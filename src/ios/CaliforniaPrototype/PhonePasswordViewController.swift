//
//  PhonePasswordViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/1/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class PhonePasswordViewController: UIViewController {

    @IBOutlet weak var passwordTextField: OutlinedTextField!
    
    var phoneNumber = String()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        
    }

    @IBAction func loginButtonTapped(_ sender: Any) {
        guard let pwdString = self.passwordTextField.text else { return }
        APIManager.sharedInstance.signInWithPhone(number: phoneNumber, password: pwdString, success: { (response) in
            guard let token = response["token"] else { return }
            guard let id = response["id"] else { return }
            _ = Keychain.set(key: "token", value: token as! String)
            _ = Keychain.set(key: "id", value: id as! String)
            UserDefaultManager.setLoggedInStatus(true)
            DispatchQueue.main.async {
                self.dismiss(animated: true, completion: nil)
            }
        }) { (error) in
            
        }
    }
    
    @IBAction func cancelButtonTapped(_ sender: Any) {
        _ = self.navigationController?.popViewController(animated: true)
    }
}
