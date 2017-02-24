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
    }

    @IBAction func canelButtonTapped(_ sender: Any) {
        self.navigationController?.setViewControllers([TabBarViewController()], animated: true)
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
