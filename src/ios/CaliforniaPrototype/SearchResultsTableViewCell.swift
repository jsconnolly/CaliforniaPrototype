//
//  SearchResultsTableViewCell.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/24/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class SearchResultsTableViewCell: UITableViewCell {

    @IBOutlet weak var locationTitle: UILabel!
    @IBOutlet weak var locationAddress: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }
    
    func configureCell(place placeString: String, address: String) -> Self {
        self.locationTitle.text = placeString
        self.locationAddress.text = address
        
        return self
    }
    
}
