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
        
        let mapTabBarItem = UITabBarItem(title: "Map", image: nil, tag: 0)
        mapNavVC.tabBarItem = mapTabBarItem
        let profileTabBarItem = UITabBarItem(title: "Profile", image: nil, tag: 0)
        profileNavVC.tabBarItem = profileTabBarItem
        
        mapNavVC.navigationBar.isTranslucent = false
        profileNavVC.navigationBar.isTranslucent = false
        self.tabBar.isTranslucent = false
        
        self.viewControllers = [mapNavVC, profileNavVC]
    }

}
