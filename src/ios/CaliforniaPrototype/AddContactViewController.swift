//
//  AddContactViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/24/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
import Contacts
import ContactsUI

class AddContactViewController: UIViewController, CNContactPickerDelegate, UITextFieldDelegate {

    @IBOutlet weak var locationDetailLabel: UILabel!
    @IBOutlet weak var enterNameTextField: OutlinedTextField!
    @IBOutlet weak var enterPhoneNumberTextField: OutlinedTextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }

    
    @IBAction func addPhoneContactButtonTapped(_ sender: Any) {
        let contactPicker = CNContactPickerViewController()
        contactPicker.delegate = self
        self.present(contactPicker, animated: true, completion: nil)
    }
    
    func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        if contact.phoneNumbers.count != 0 {
            for number in contact.phoneNumbers {
                if number.label == "_$!<Mobile>!$_" {
                    self.enterPhoneNumberTextField.text = number.value.stringValue
                    break
                } else {
                    self.enterPhoneNumberTextField.text = number.value.stringValue
                }
            }
        }
        var nameString = String()
        if !contact.givenName.isEmpty {
            nameString.append(contact.givenName)
        }
        if !contact.familyName.isEmpty {
            nameString.append(" \(contact.familyName)")
        }
    }
    
    func retrieveContactsWithStore(_ store: CNContactStore) {
        do {
            let groups = try store.groups(matching: nil)
            let predicate = CNContact.predicateForContactsInGroup(withIdentifier: groups[0].identifier)
            let keysToFetch = [CNContactFormatter.descriptorForRequiredKeys(for: .fullName), CNContactPhoneNumbersKey as CNKeyDescriptor, CNContactGivenNameKey as CNKeyDescriptor, CNContactFamilyNameKey as CNKeyDescriptor]
            _ = try store.unifiedContacts(matching: predicate, keysToFetch: keysToFetch)
        } catch {
            print(error)
        }
    }
    
}
