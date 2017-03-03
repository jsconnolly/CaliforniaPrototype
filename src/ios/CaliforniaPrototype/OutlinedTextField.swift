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
        layer.borderColor = UIColor.textFieldBorderGray().cgColor
    }
    
    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return CGRect(x: bounds.origin.x + 8,
                      y: bounds.origin.y,
                      width: bounds.width - 8,
                      height: bounds.height)
    }
    
    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return CGRect(x: bounds.origin.x + 8,
                      y: bounds.origin.y,
                      width: bounds.width - 8,
                      height: bounds.height)
    }

}
