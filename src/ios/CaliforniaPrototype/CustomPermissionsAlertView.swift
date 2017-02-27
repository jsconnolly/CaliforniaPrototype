//
//  CustomPermissionsAlertView.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
@IBDesignable
class CustomPermissionsAlertView: UIView {

    override func draw(_ rect: CGRect) {
        CustomAlertBackground.drawArtboard(frame: rect, resizing: .aspectFit)
    }

}
