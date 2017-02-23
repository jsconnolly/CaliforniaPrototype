//
//  LandingViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/21/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class LandingViewController: UIViewController {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.isNavigationBarHidden = true
    }
    
    @IBAction func continueButtonTapped(_ sender: Any) {
    }
    
    @IBAction func loginWithPhoneButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(PhoneLoginViewController(), animated: true)
    }
    
    @IBAction func loginWithEmailButtonTapped(_ sender: Any) {
        self.navigationController?.pushViewController(EmailLoginViewController(), animated: true)
    }
    
}
