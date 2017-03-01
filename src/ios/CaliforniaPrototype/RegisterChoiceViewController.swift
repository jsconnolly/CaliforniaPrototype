//
//  RegisterChoiceViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/28/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class RegisterChoiceViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }
    
    @IBAction func registerWithEmailButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(EmailRegistrationViewController(), animated: true)
    }
    
    @IBAction func registerWithPhoneButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(PhoneRegistrationViewController(), animated: true)
    }
    
    @IBAction func loginWithEmailButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(EmailLoginViewController(), animated: true)
    }
    
    @IBAction func loginWithPhoneButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(PhoneLoginViewController(), animated: true)
    }
    

}
