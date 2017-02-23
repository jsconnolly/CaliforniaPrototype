//
//  PhoneLoginViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/22/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class PhoneLoginViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var phoneTextField: OutlinedTextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.stackView.arrangedSubviews[1].isHidden = true
    }

    
    @IBAction func continueButtonTapped(_ sender: Any) {
        self.phoneTextField.resignFirstResponder()
        guard let phoneString = self.phoneTextField.text else { return }
        if !ValidationMethods().isValidPhoneNumber(phoneString) {
            self.phoneTextField.layer.borderColor = UIColor.red.cgColor
            if self.stackView.arrangedSubviews[1].isHidden == true {
                self.animateStackSubview(1, to: false)
            }
        } else {
            self.phoneTextField.layer.borderColor = UIColor.black.cgColor
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
            self.navigationController?.pushViewController(PhoneVerificationViewController(), animated: true)
        }
    }
    
    func animateStackSubview(_ viewNumber: Int,to bool: Bool) {
        self.stackView.arrangedSubviews[viewNumber].isHidden = bool
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
    
    @IBAction func emailLoginButtonTapped(_ sender: Any) {
        
    }

    @IBAction func noAccountButtonTapped(_ sender: Any) {
        
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
    
    
    func setTextFieldBorderActive(_ textField: UITextField) {
        if textField == self.phoneTextField {
            textField.layer.borderColor = UIColor.black.cgColor
            self.phoneTextField.layer.borderColor = UIColor.gray.cgColor
        } else {
            textField.layer.borderColor = UIColor.black.cgColor
            self.phoneTextField.layer.borderColor = UIColor.gray.cgColor
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
    }
}
