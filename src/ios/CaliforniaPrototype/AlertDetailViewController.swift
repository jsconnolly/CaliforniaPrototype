//
//  AlertDetailViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/2/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class AlertDetailViewController: UIViewController {

    @IBOutlet weak var locationLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var typeLabel: UILabel!

    var alert : Alert?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setTextFields()
    }
    
    func setTextFields() {
        self.locationLabel.text = alert?.location
        self.dateLabel.text = alert?.date
        self.nameLabel.text = alert?.name
        self.typeLabel.text = alert?.type
    }

}
