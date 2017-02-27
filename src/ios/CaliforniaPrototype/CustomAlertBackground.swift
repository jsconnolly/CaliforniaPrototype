//
//  CustomAlertBackground.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class CustomAlertBackground: NSObject {
    //MARK: - Canvas Drawings
    
    /// Page 1
    
    class func drawArtboard(frame targetFrame: CGRect = CGRect(x: 0, y: 0, width: 270, height: 180), resizing: ResizingBehavior = .aspectFit) {
        /// General Declarations
        let context = UIGraphicsGetCurrentContext()!
        
        /// Resize to Target Frame
        context.saveGState()
        let resizedFrame = resizing.apply(rect: CGRect(x: 0, y: 0, width: 270, height: 180), target: targetFrame)
        context.translateBy(x: resizedFrame.minX, y: resizedFrame.minY)
        context.scaleBy(x: resizedFrame.width / 270, y: resizedFrame.height / 180)
        
        /// Group 5
        do {
            context.saveGState()
            
            /// Rectangle 4
            let rectangle4 = UIBezierPath(roundedRect: CGRect(x: 0, y: 0, width: 270, height: 180), cornerRadius: 12)
            context.saveGState()
            UIColor(white: 0.992, alpha: 1).setFill()
            rectangle4.fill()
            context.saveGState()
            rectangle4.lineWidth = 2
            context.beginPath()
            context.addPath(rectangle4.cgPath)
            context.clip(using: .evenOdd)
            UIColor(hue: 0.083, saturation: 0.01, brightness: 0.816, alpha: 1).setStroke()
            rectangle4.stroke()
            context.restoreGState()
            context.restoreGState()
            
            /// Line
            let line = UIBezierPath()
            line.move(to: CGPoint(x: 0, y: 1))
            line.addLine(to: CGPoint(x: 268.38, y: 1))
            context.saveGState()
            context.translateBy(x: 134.81, y: 134.5)
            context.scaleBy(x: -1, y: -1)
            context.rotate(by: 2 * CGFloat.pi)
            context.translateBy(x: -134.19, y: -1)
            line.lineCapStyle = .square
            line.lineWidth = 0.75
            UIColor(hue: 0.083, saturation: 0.01, brightness: 0.816, alpha: 1).setStroke()
            line.stroke()
            context.restoreGState()
            
            /// Line
            let line2 = UIBezierPath()
            line2.move(to: CGPoint(x: 1, y: 0))
            line2.addLine(to: CGPoint(x: 1, y: 44))
            context.saveGState()
            context.translateBy(x: 133.38, y: 135)
            line2.lineCapStyle = .square
            line2.lineWidth = 0.75
            UIColor(hue: 0.083, saturation: 0.01, brightness: 0.816, alpha: 1).setStroke()
            line2.stroke()
            context.restoreGState()
            
            context.restoreGState()
        }
        
        context.restoreGState()
    }
    
    
    //MARK: - Canvas Images
    
    /// Page 1
    
    class func imageOfArtboard() -> UIImage {
        struct LocalCache {
            static var image: UIImage!
        }
        if LocalCache.image != nil {
            return LocalCache.image
        }
        var image: UIImage
        
        UIGraphicsBeginImageContextWithOptions(CGSize(width: 270, height: 180), false, 0)
        CustomAlertBackground.drawArtboard()
        image = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        
        LocalCache.image = image
        return image
    }
    
    //MARK: - Resizing Behavior
    
    enum ResizingBehavior {
        case aspectFit /// The content is proportionally resized to fit into the target rectangle.
        case aspectFill /// The content is proportionally resized to completely fill the target rectangle.
        case stretch /// The content is stretched to match the entire target rectangle.
        case center /// The content is centered in the target rectangle, but it is NOT resized.
        
        func apply(rect: CGRect, target: CGRect) -> CGRect {
            if rect == target || target == CGRect.zero {
                return rect
            }
            
            var scales = CGSize.zero
            scales.width = abs(target.width / rect.width)
            scales.height = abs(target.height / rect.height)
            
            switch self {
            case .aspectFit:
                scales.width = min(scales.width, scales.height)
                scales.height = scales.width
            case .aspectFill:
                scales.width = max(scales.width, scales.height)
                scales.height = scales.width
            case .stretch:
                break
            case .center:
                scales.width = 1
                scales.height = 1
            }
            
            var result = rect.standardized
            result.size.width *= scales.width
            result.size.height *= scales.height
            result.origin.x = target.minX + (target.width - result.width) / 2
            result.origin.y = target.minY + (target.height - result.height) / 2
            return result
        }
    }

}
