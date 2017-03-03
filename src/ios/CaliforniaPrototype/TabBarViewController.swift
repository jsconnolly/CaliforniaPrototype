//
//  TabBarViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class TabBarViewController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        let mapNavVC = UINavigationController(rootViewController: MapViewController())
        let profileNavVC = UINavigationController(rootViewController: ProfileViewController())
        
        //Create tab bar items for individual tabs
        let mapTabBarItem = UITabBarItem(title: "Map", image: UIImage.init(named: "mapDeselectedIcon")?.withRenderingMode(.alwaysOriginal), tag: 0)
        mapTabBarItem.selectedImage = UIImage.init(named: "mapSelectedIcon")?.withRenderingMode(.alwaysOriginal)
        mapNavVC.tabBarItem = mapTabBarItem
        
        let profileTabBarItem = UITabBarItem(title: "Profile", image: UIImage.init(named: "profileDeselectedIcon")?.withRenderingMode(.alwaysOriginal), tag: 0)
        profileTabBarItem.selectedImage = UIImage.init(named: "profileSelectedIcon")?.withRenderingMode(.alwaysOriginal)
        profileNavVC.tabBarItem = profileTabBarItem
        
        //set appearance customizations for tab and nav bar
        mapNavVC.navigationBar.isTranslucent = false
        profileNavVC.navigationBar.isTranslucent = false
        self.tabBar.isTranslucent = false
        
        let tabItemSize = self.tabBar.frame
        let itemSize = CGSize.init(width: tabItemSize.width/2, height: tabItemSize.height)
        self.tabBar.selectionIndicatorImage = self.makeImageWithColorAndSize(UIColor.lightGrayBkgrnd(), size: itemSize)
        
        self.viewControllers = [mapNavVC, profileNavVC]
        
        //set tab item title selected/deselected colors
        for item in self.tabBar.items! {
            item.setTitleTextAttributes([NSForegroundColorAttributeName : UIColor.lightGray], for: .normal)
            item.setTitleTextAttributes([NSForegroundColorAttributeName : UIColor.black], for: .selected)
        }
    }
    
    func makeImageWithColorAndSize(_ color: UIColor, size: CGSize) -> UIImage? {
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        color.setFill()
        UIRectFill(CGRect(x: 0, y: 0, width: size.width, height: size.height))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        if let image = image {
            return image
        }
        return nil
    }

}
