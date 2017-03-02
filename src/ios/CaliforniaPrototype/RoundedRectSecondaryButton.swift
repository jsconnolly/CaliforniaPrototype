//
//  RoundedRectSecondaryButton.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/2/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class RoundedRectSecondaryButton: UIButton {

    override public func awakeFromNib() {
        layer.cornerRadius = 4.0
        self.backgroundColor = UIColor.hexStringToUIColor(hex: "aeb0b5")
        self.setTitleColor(UIColor.white, for: .normal)
    }


}
