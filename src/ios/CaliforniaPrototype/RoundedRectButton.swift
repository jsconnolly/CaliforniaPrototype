//
//  RoundedRectButton.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/21/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class RoundedRectButton: UIButton {

    override public func awakeFromNib() {
        layer.cornerRadius = 4.0
        self.backgroundColor = UIColor.hexStringToUIColor(hex: "0071bc")
        self.setTitleColor(UIColor.white, for: .normal)
    }

}
