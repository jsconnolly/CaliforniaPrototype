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
        if let dateTimeString = self.alert?.date {
            self.dateLabel.text = formatDateString(dateTimeString)
        } else {
            self.dateLabel.text = self.alert?.date
        }
        self.nameLabel.text = alert?.name
        self.typeLabel.text = alert?.type
    }
    
    func formatDateString(_ date: String) -> String {
        var dateString = String()
        let needle: Character = "T"
        if let idx = date.characters.index(of: needle) {
            dateString = date.substring(to: idx)
        } else {
            dateString = date
        }
        
        let dateNeedle: Character = "T"
        let timeNeedle: Character = "."
        if let dateIdx = date.characters.index(of: dateNeedle) {
            var time = date.substring(from: dateIdx)
            if let timeIdx = time.characters.index(of: timeNeedle) {
                time = time.substring(to: timeIdx)
                time  = time.replacingOccurrences(of: "T", with: "")
                dateString = dateString + "   " + time
            }
        } else {
            dateString = date
        }
        
        return dateString
    }

}
