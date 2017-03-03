#CREDENTIALS AND LINKS

##Links

<b>Admin Website:</b> <a href='http://ca-prototype-hotb.s3-website-us-west-1.amazonaws.com/admin'>Click Here</a>

<b>Consumer Website:</b> <a href='http://ca-prototype-hotb.s3-website-us-west-1.amazonaws.com'>Click Here</a>

##User Accounts

<b>Consumer Account:</b> Can be created using any valid email

<b>Admin Account:</b> Username: admin@admin.com; PW: password1

#TECHNICAL APPROACH

HOTB Software Solutions developed the California Emergency Alert Prototype in response to the California Department of Technology ADPQ Vendor Pool RFI Submission due on March 3, 2017.

Our technical approach was based on HOTB’s agile development process that utilizes Scrum Methodology focused on short, iterative sprints to deliver functional components that are evolved based on user feedback. We started with the concept of delivering a functional prototype that would enable consumers to create a user account and sign up to receive emergency and non-emergency notifications, via email and SMS, that were associated with locations that were relevant to the user. In addition, we wanted to create an administrative dashboard that would allow authorized administrators to create manual alerts and review system generated alerts from publicly available APIs provided by the state’s procurement department. Our goal was to deliver this prototype with a clear and intuitive design that was driven by user feedback and delivered in an agile software development environment that leveraged modern, open-source technologies.

Our design process incorporated several user-centric design methodologies that provided feedback which was incorporated into our subsequent design iterations. In an effort to highlight our broader capabilities, we also developed native iOS and Android applications that connect to the prototype APIs. Although these are not a part of our formal submission, as they were never submitted to their respective App Stores, you will see code commits for each of these applications in our Github repository along with instructions on how to install fully-functional versions of them or to request a test flight version.

We chose a modern technology stack that utilized open source technologies and implemented those using a continuous integration and continuous deployment methodology based on Docker containers and Jenkins. Automated tests scripts were either written in Python or created using Selenium and those were executed against the prototype during the development process.

The project team met daily to review project status, action items and deliverables. Progress was tracked using Aha!, our internal project management and ticketing system, and we relied upon Slack as our collaboration tool. All code updates were committed to Github along with our design iterations and test scripts.

Further information about our process can be found in the California RFI Process Documentation attached <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/California+Prototype.pdf'>here</a> and in our responses below.

#ARCHITECTURAL FLOW
<a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/CaliforniaPrototype-InfrastructureDiagram-Current.jpg'>here</a>

#US DIGITAL SERVICES PLAYBOOK

We followed the US Digital Services Playbook. Our responses to each can be found <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/Play+Book.pdf'>here</a>

#OTHER REQUESTED ITEMS

##a. Assigned one (1) leader and gave that person authority and responsibility and held that person accountable for the quality of the prototype submitted;

We assigned the role of Project Lead to Jason Connolly. Jason acted as the Business Owner and established project priorities and acted as the primary leader taking accountability for all phases of the project.

##b. Assembled a multidisciplinary and collaborative team that includes, at a minimum, five (5) of the labor categories as identified in Attachment B: PQVP DS-AD Labor Category Descriptions;

1. Project Lead – Jason Connolly

2. Product Manager – Mark Witte

3. Technical Architect – Mike Firoved

4. Interaction Designer/User Researcher/Usability Tester – Todd Schermerhorn

5. Visual Designer – Dan Castellon

6. Front End Web Developer – Kristi Witts, Raghu Jonnala, Patrick MacDowell, Luis Garcia

7. Backend Web Developer – Jason Huang

8. DevOps Engineer – Eric Dobyns

##c. Understood what people needed, by including people in the prototype development and design process;

During our initial discovery process we utilized surveys and questionnaires conducted with consumers and prospective users which helped shape our understanding of user requirements and key features. We also conducted a phone interview with Gary Winuk, former Chief Deputy Director and General Counsel for the Office of Homeland Security (OHS), to review the initial wireframes for the admin portal. His experience and insights were extremely valuable in shaping the features and functions for that side of the platform

We constructed an initial user survey to get feedback on general user experience with emergency notification services. Results of the survey are included <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/California+Emergency+%26+Non+Emergency+Alerts+Survey+-+Google+Forms+copy.pdf'>here</a>  

We also had users completed questionnaires following a user session with a clickable prototype. A sample of those responses can be found <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/User+Testing+Interview+for+CA+Prototype+-+Google+Docs.pdf'>here</a>.

Interview Notes with Gary Winuk can be found <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/Gary+Winuk+Interview+-+Google+Docs.pdf'>here</a>.

##d. Used at least a minimum of three (3) “user-centric design” techniques and/or tools;

We used at least three (3) user centric design techniques that included: Key Experience Pillar Documents, Online Survey, User Questionnaires following a clickable prototype review and phone interviews with subject matter experts to gather feedback on the Administrative Portal’s features and functions.

In addition to the surveys and interviews above, we created Key Experience Pillar documents to define user stories and use cases. Those can be found for both the consumer <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/User+Experience+Pillars+Document+-+Google+Docs.pdf'>here</a> and the administrator <a href='
https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/Admin+Experience+Pillars+Document+-+Google+Docs.pdf'>here</a>.

##e. Used GitHub to document code commits;

A GitHub repository was setup to capture code commits for each developer. We also included design commits and automated testing scripts. We utilized version control by separating branches for each developer as well as managed code merges through individual pull requests.

##f. Used Swagger to document the RESTful API, and provided a link to the Swagger API;

Swagger was used to document our RESTful APIs and other technical documentation. A link to our Swagger account can be found <a href='http://ec2-52-53-93-51.us-west-1.compute.amazonaws.com:8080/'>here</a>.

##g. Complied with Section 508 of the Americans with Disabilities Act and WCAG 2.0;

We created our and implemented our designs and user experience in accordance with standards outlined in Section 508 of the ADA and WCAG 2.0.

##h. Created or used a design style guide and/or a pattern library;

Following the initial wireframe creation, we created a <a href='https://s3-us-west-1.amazonaws.com/ca-prototype-hotb-assets/Styleguide.pdf'>style guide and color palette</a> that was used throughout the application for consistency and uniformity. 

##i. Performed usability tests with people;

We conducted usability tests with both consumer and technical experts. On the consumer side we interviewed two single mothers with children. On the technical side we interviewed two software engineers with expertise in messaging systems and intuitive user interface design. Each group provided relevant feedback that sharpened our focus and improved our design and system features.

##j. Used an iterative approach, where feedback informed subsequent work or versions of the prototype;

Our initial designs were based on Key Experience Pillars documents created for both the consumer and administrator sections of the application. These documents were created and based on feedback we received from our initial consumer survey. We made adjustments to the design and system features following receipt of our user questionnaires and phone interview with Gary Winuk.

##k. Created a prototype that works on multiple devices, and presents a responsive design;

The prototype works across multiple devices (phones, tablets and PCs) and is supported by multiple browsers and is responsive to the specific device.

##l. Used at least five (5) modern and open-source technologies, regardless of architectural layer (frontend, backend, etc.);

We used at least five (5) modern and open-source technologies including:

###Front-End

*Bootstrap

###API/Back-End

*NodeJS

*MongoDB

###Dev-Ops

*Jenkins

*Docker

*Selenium

*Python


##m. Deployed the prototype on an Infrastructure as a Service (IaaS) or Platform as Service (PaaS) provider, and indicated which provider they used;

Our API is built using dockerized Node.js containers deployed on Amazon EC2 Instances. The EC2 instances are managed with Amazon ECS and auto-scale based on incoming traffic. All incoming traffic is passed through an elastic load balancer which automatically distributes incoming application traffic across multiple Amazon EC2 instances.

Our database is comprised of a MongoDB cluster with three replica sets to ensure high availability in case one of the servers goes down.

##n. Developed automated unit tests for their code;

We developed automated unit UI tests written in Python that ran on an instance of Selenium. Continuous API tests, which are also written in python, are initiated through Jenkins following the automated build and deployment process.

##o. Setup or used a continuous integration system to automate the running of tests and continuously deployed their code to their IaaS or PaaS provider;

We setup Docker to containerize our server environment to handle continuous deployments. Deployments were automatically kicked off and managed through Jenkins following each commitment to a development branch. Automated tests built with Selenium were initiated following each deployment. Some of these jobs consist of deploying dockerized Node.js containers to our staging and production AWS ECS Clusters; making nightly backups of the staging and production MongoDB databases; executing NPM, Bower and Grunt tasks to build and deploy our website to staging and production AWS S3 buckets; and running automated tests to validate previous updates. In addition to automated tests, our QA engineers conduct manual testing of each update across all platforms. 

##p. Setup or used configuration management;

Jenkins and Docker files were used for configuration management.

##q. Setup or used continuous monitoring;

AWS Cloudwatch was setup to monitor the servers, application availability and general system performance.

##r. Deployed their software in an open source container, such as Docker (i.e., utilized operating-system-level virtualization);

We utilized Docker for our containerization.

##s. Provided sufficient documentation to install and run their prototype on another machine; 

See individual README.md files for each component.

##t. Prototype and underlying platforms used to create and run the prototype are openly licensed and free of charge.

All components used to deliver and run the prototype are openly licensed and free of charge.

