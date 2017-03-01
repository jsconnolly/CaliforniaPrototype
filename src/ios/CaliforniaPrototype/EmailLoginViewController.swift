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
    
    private var spinner : UIActivityIndicatorView?

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
        
        if ValidationMethods().isValidPassword(passwordString) && ValidationMethods().isValidEmail(emailString) {
            self.spinner = UIActivityIndicatorView(activityIndicatorStyle: .gray)
            self.spinner?.center = self.view.center
            self.spinner?.hidesWhenStopped = true
            self.spinner?.startAnimating()
            APIManager.sharedInstance.signInWithEmail(email: emailString, password: passwordString, success: { (response) in
                self.spinner?.stopAnimating()
                guard let token = response["token"] else { return }
                if Keychain.set(key: "token", value: token as! String) {
                    DispatchQueue.main.async {
                        self.navigationController?.setViewControllers([TabBarViewController()], animated: true)
                    }
                } else {
                    DispatchQueue.main.async {
                        let alert = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error logging in, please try again.")
                        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                }
            }, failure: { (error) in
                self.spinner?.stopAnimating()
                if error?.code == 404 {
                    DispatchQueue.main.async {
                        let alert = CustomAlertControllers.controllerWith(title: "Error", message: "You are not a registered user, please register before signing in.")
                        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                }
            })
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
            self.passwordTextField.layer.borderColor = UIColor.textFieldBorderGray().cgColor
        } else {
            textField.layer.borderColor = UIColor.black.cgColor
            self.emailTextField.layer.borderColor = UIColor.textFieldBorderGray().cgColor
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
    }
    
}
