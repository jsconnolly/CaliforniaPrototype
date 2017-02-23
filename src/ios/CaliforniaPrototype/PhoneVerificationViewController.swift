//
//  PhoneVerificationViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/22/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class PhoneVerificationViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var phoneNumberLabel: UILabel!
    
    @IBOutlet weak var firstCodeTextField: OutlinedTextField!
    @IBOutlet weak var secondCodeTextField: OutlinedTextField!
    @IBOutlet weak var thirdCodeTextField: OutlinedTextField!
    @IBOutlet weak var fourthCodeTextField: OutlinedTextField!
    @IBOutlet weak var fifthCodeTextField: OutlinedTextField!
    @IBOutlet weak var sixthCodeTextField: OutlinedTextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationController?.isNavigationBarHidden = true
    }

    
    @IBAction func editNumberButtonTapped(_ sender: Any) {
       _ = self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func verifyButtonTapped(_ sender: Any) {
        
    }
    
    @IBAction func resendCodeButtonTapped(_ sender: Any) {
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let count = textField.text?.characters.count else { return false }
        let newLength = count + string.characters.count - range.length
        
        return newLength <= 1
        
    }
    
    
    
}
