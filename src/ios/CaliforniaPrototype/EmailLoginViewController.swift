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
    
    private var spinner = UIActivityIndicatorView()

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
        
        if ValidationMethods().isValidEmail(emailString) {
            self.spinner = UIActivityIndicatorView(activityIndicatorStyle: .gray)
            self.spinner.center = self.view.center
            self.spinner.hidesWhenStopped = true
            self.spinner.startAnimating()
            self.view.addSubview(self.spinner)
            APIManager.sharedInstance.signInWithEmail(email: emailString, password: passwordString, success: { (response) in
                self.spinner.stopAnimating()
                guard let token = response["token"] else { return }
                guard let id = response["id"] else { return }
                UserManager.loginAndSave(userId: id as! String, token: token as! String)
                DispatchQueue.main.async {
                    self.dismiss(animated: true, completion: nil)
                }
            }, failure: { (error) in
                    DispatchQueue.main.async {
                        self.spinner.stopAnimating()
                        let alert = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error signing in. Please make sure your email and password are correct.")
                        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
            })
        }
    }
    @IBAction func loginWithPhoneButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(PhoneLoginViewController(), animated: true)
    }
    
    @IBAction func createAnAccountButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(RegisterChoiceViewController(), animated: true)
    }
    
    @IBAction func forgotButtonTapped(_ sender: Any) {
        guard let emailText = self.emailTextField.text else { return }
        if emailText.isEmpty || emailText == " " {
            let alert = CustomAlertControllers.controllerWith(title: "Enter Email", message: "Enter your email and tap on the Forgot My Password button again.")
            let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okAction)
            self.present(alert, animated: true, completion: nil)
        }
        APIManager.sharedInstance.forgotPassword(emailText, success: { (response) in
            DispatchQueue.main.async {
                let alert = CustomAlertControllers.controllerWith(title: "Email Sent", message: "An email has been sent to you, please check your email to reset your password.")
                let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                alert.addAction(okAction)
                self.present(alert, animated: true, completion: nil)
            }
        }) { (error) in
            DispatchQueue.main.async {
                let alert = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error sending the email, please try again.")
                let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                alert.addAction(okAction)
                self.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    @IBAction func backButtonTapped(_ sender: Any) {
        _ = self.navigationController?.popViewController(animated: true)
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
