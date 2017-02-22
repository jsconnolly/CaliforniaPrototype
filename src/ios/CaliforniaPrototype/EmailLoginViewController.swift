//
//  EmailLoginViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/22/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class EmailLoginViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var emailTextField: OutlinedTextField!
    @IBOutlet weak var passwordTextField: OutlinedTextField!
    @IBOutlet weak var stackView: UIStackView!

//MARK: - UIViewController Delegate methods
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.isNavigationBarHidden = true
        
        self.stackView.arrangedSubviews[1].isHidden = true
        self.stackView.arrangedSubviews[3].isHidden = true
    }
    
    
    @IBAction func continueButtonTapped(_ sender: Any) {
        guard let emailString = self.emailTextField.text, let passwordString = self.passwordTextField.text else { return }
        
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
    }
    
    func animateStackSubview(_ viewNumber: Int,to bool: Bool) {
        self.stackView.arrangedSubviews[viewNumber].isHidden = bool
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
    
//MARK: - UITextField Delegate methods
    func textFieldDidBeginEditing(_ textField: UITextField) {
        self.setTextFieldBorderActive(textField)
        if textField == self.emailTextField {
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
        } else {
            if self.stackView.arrangedSubviews[3].isHidden == false {
                self.animateStackSubview(3, to: true)
            }
        }
    }
    
    func setTextFieldBorderRed(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.red.cgColor
    }
    
    func setTextFieldBorderBlack(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.black.cgColor
    }
    
    func setTextFieldBorderActive(_ textField: UITextField) {
        if textField == self.emailTextField {
            textField.layer.borderColor = UIColor.black.cgColor
            self.passwordTextField.layer.borderColor = UIColor.gray.cgColor
        } else {
            textField.layer.borderColor = UIColor.black.cgColor
            self.emailTextField.layer.borderColor = UIColor.gray.cgColor
        }
    }
    
}
