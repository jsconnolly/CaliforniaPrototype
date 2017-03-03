//
//  EmailRegistrationViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class EmailRegistrationViewController: UIViewController {

    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var emailTextField: OutlinedTextField!
    @IBOutlet weak var passwordTextField: OutlinedTextField!
    @IBOutlet weak var nameTextField: OutlinedTextField!
    
    private var spinner = UIActivityIndicatorView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationController?.isNavigationBarHidden = true
        
        self.stackView.arrangedSubviews[1].isHidden = true
        self.stackView.arrangedSubviews[3].isHidden = true
        self.stackView.arrangedSubviews[5].isHidden = true
    }
    
    @IBAction func continueButtonTapped(_ sender: Any) {
        guard let emailString = self.emailTextField.text, let passwordString = self.passwordTextField.text, let nameString = self.nameTextField.text else { return }
        
        if !ValidationMethods().isValidEmail(emailString) {
            self.setTextFieldBorderRed(self.emailTextField)
            if self.stackView.arrangedSubviews[1].isHidden == true {
                self.animateStackSubview(1, to: false)
            }
        } else {
            self.setTextFieldBorderBlack(self.emailTextField)
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
        }
        
        if !ValidationMethods().isValidPassword(passwordString) {
            self.setTextFieldBorderRed(self.passwordTextField)
            if self.stackView.arrangedSubviews[3].isHidden == true {
                self.animateStackSubview(3, to: false)
            }
        } else {
            self.setTextFieldBorderBlack(self.passwordTextField)
            if self.stackView.arrangedSubviews[3].isHidden == false {
                self.animateStackSubview(3, to: true)
            }
        }
        
        if nameString.isEmpty || nameString == " " {
            if self.stackView.arrangedSubviews[5].isHidden == true {
                self.animateStackSubview(5, to: false)
            }
        }
        
        if ValidationMethods().isValidEmail(emailString) && ValidationMethods().isValidPassword(passwordString) {
            var textName = String()
            if let name = self.nameTextField.text {
                if !name.isEmpty {
                    textName = name
                } else {
                    textName = ""
                }
            }
            self.spinner = UIActivityIndicatorView(activityIndicatorStyle: .gray)
            self.spinner.center = self.view.center
            self.spinner.hidesWhenStopped = true
            self.spinner.startAnimating()
            self.view.addSubview(self.spinner)
            APIManager.sharedInstance.registerUserWith(email: emailString, password: passwordString, name: textName, phone: nil, address: nil, city: nil, state: nil, zip: nil, success: { (response) in
                self.spinner.stopAnimating()
                guard let id = response?.id else { return }
                guard let token = response?.token else { return }
                UserManager.loginAndSave(userId: id, token: token)
                DispatchQueue.main.async {
                    self.navigationController?.pushViewController(LocationPermissionsViewController(), animated: true)
                }
                
            }, failure: { (error) in
                let alert = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error registering, please try again.")
                let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                alert.addAction(okAction)
                DispatchQueue.main.async {
                    self.spinner.stopAnimating()
                    self.present(alert, animated: true, completion: nil)
                }
            })
            
        }
    }

    @IBAction func canelButtonTapped(_ sender: Any) {
        _ = self.navigationController?.popViewController(animated: true)
    }
}


extension EmailRegistrationViewController: UITextFieldDelegate {
    //MARK: - UITextField Delegate methods
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.black.cgColor
        if textField == self.emailTextField {
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
        }
        
        if textField == self.passwordTextField {
            if self.stackView.arrangedSubviews[3].isHidden == false {
                self.animateStackSubview(3, to: true)
            }
        }
        
        if textField == self.nameTextField {
            if self.stackView.arrangedSubviews[5].isHidden == false {
                self.animateStackSubview(5, to: true)
            }
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.textFieldBorderGray().cgColor
    }
    
    func animateStackSubview(_ viewNumber: Int,to bool: Bool) {
        self.stackView.arrangedSubviews[viewNumber].isHidden = bool
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
    
    func setTextFieldBorderRed(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.red.cgColor
    }
    
    func setTextFieldBorderBlack(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.black.cgColor
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
    }
}
