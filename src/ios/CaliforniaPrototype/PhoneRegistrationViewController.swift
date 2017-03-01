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
            
            let phoneString = "1" + phoneTextFieldString
            APIManager.sharedInstance.registerUserWithPhone(number: phoneString, success: { (response : [String : Any?]) in
                if response["registered"] as! String == "success" {
                    DispatchQueue.main.async {
                        let verificationVC = PhoneVerificationViewController()
                        verificationVC.phoneNumber = phoneString
                        verificationVC.phoneNumberLabel.text = phoneString
                        self.navigationController?.pushViewController(verificationVC, animated: true)
                    }
                }
            }, failure: { (error) in
                
            })
            
        }
    }
    
    @IBAction func cancelButtonTapped(_ sender: Any) {
        _ = self.navigationController?.popViewController(animated: true)
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
}
