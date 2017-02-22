//
//  OutlinedTextField.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/22/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class OutlinedTextField: UITextField {

    override public func awakeFromNib() {
        layer.cornerRadius = 4.0
        layer.borderWidth = 1.0
        layer.borderColor = UIColor.darkGray.cgColor
    }

}
