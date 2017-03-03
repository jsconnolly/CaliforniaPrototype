//
//  PhoneRegistrationViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/28/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class PhoneRegistrationViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var phoneTextField: OutlinedTextField!
    
    private var spinner = UIActivityIndicatorView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.stackView.arrangedSubviews[1].isHidden = true
    }
    
    @IBAction func continueButtonTapped(_ sender: Any) {
        self.phoneTextField.resignFirstResponder()
        guard let phoneTextFieldString = self.phoneTextField.text else { return }
        if !ValidationMethods().isValidPhoneNumber(phoneTextFieldString) {
            self.phoneTextField.layer.borderColor = UIColor.red.cgColor
            if self.stackView.arrangedSubviews[1].isHidden == true {
                self.animateStackSubview(1, to: false)
            }
        } else {
            self.phoneTextField.layer.borderColor = UIColor.black.cgColor
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
            
            self.showAndStartSpinner()
            let phoneString = "1" + phoneTextFieldString
            APIManager.sharedInstance.registerUserWithPhone(number: phoneString, success: { (response : [String : Any?]) in
                if response["registered"] as! String == "success" {
                    DispatchQueue.main.async {
                        self.stopAndRemoveSpinner()
                        let verificationVC = PhoneVerificationViewController()
                        verificationVC.phoneNumber = phoneString
                        self.navigationController?.pushViewController(verificationVC, animated: true)
                    }
                }
            }, failure: { (error) in
                self.stopAndRemoveSpinner()
            })
            
        }
    }
    
    @IBAction func cancelButtonTapped(_ sender: Any) {
        _ = self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func registerWithEmailButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(EmailRegistrationViewController(), animated: true)
    }
    
    
    

    func animateStackSubview(_ viewNumber: Int,to bool: Bool) {
        self.stackView.arrangedSubviews[viewNumber].isHidden = bool
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
    
    //MARK: - UITextField delegate methods
    func textFieldDidBeginEditing(_ textField: UITextField) {
        self.setTextFieldBorderActive(textField)
        if textField == self.phoneTextField {
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
        }
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let count = textField.text?.characters.count else { return false }
        let newLength = count + string.characters.count - range.length
        
        if newLength <= 10 {
            return true
        } else {
            return false
        }
    }
    
    func setTextFieldBorderActive(_ textField: UITextField) {
        if textField == self.phoneTextField {
            textField.layer.borderColor = UIColor.black.cgColor
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
        self.phoneTextField.layer.borderColor = UIColor.textFieldBorderGray().cgColor
    }
    
    //MARK: - Activity Indicator Methods
    func showAndStartSpinner() {
        self.spinner = UIActivityIndicatorView(activityIndicatorStyle: .gray)
        self.spinner.center = self.view.center
        self.spinner.hidesWhenStopped = true
        self.spinner.startAnimating()
        self.view.addSubview(self.spinner)
    }
    
    func stopAndRemoveSpinner() {
        self.spinner.stopAnimating()
        self.spinner.removeFromSuperview()
    }
}
