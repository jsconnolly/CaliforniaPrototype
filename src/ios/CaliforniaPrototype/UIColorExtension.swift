//
//  UIColorExtension.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation
import UIKit

extension UIColor {
    
    class func textFieldBorderGray() -> UIColor {
        return UIColor.colorWithRedValue(redValue: 196, greenValue: 200, blueValue: 204, alpha: 1.0)
    }
    
    class func lightGrayBkgrnd() -> UIColor {
        return UIColor.colorWithRedValue(redValue: 230, greenValue: 230, blueValue: 230, alpha: 1.0)
    }
    
    class func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.characters.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    
    private class func colorWithRedValue(redValue: CGFloat, greenValue: CGFloat, blueValue: CGFloat, alpha: CGFloat) -> UIColor {
        return UIColor(red: redValue/255.0, green: greenValue/255.0, blue: blueValue/255.0, alpha: alpha)
    }
}
