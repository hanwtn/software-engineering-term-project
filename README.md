# CMPT276 Group 3 Proposal
02/25/2024, version 1.0 Prototype

## Summary/Abstract:

A web app designed to help trainers and clients streamline their fitness routines. Trainers can easily track clients, assign workouts, and monitor progress, while clients benefit from personalized programs and engaging in a supportive community. Features include video logging, real-time tracking, fitness data analytics, and a gamified scoring system for added motivation. Overall, the app seeks to enhance efficiency, save costs, increase community engagement, and encourage consistent fitness progress.

## Our group (backgrounds/experience)

Nolan: I have experience in Angular, HTML, CSS, SQL, and javascript/java, and have made several small web-based projects. Worked on 1-2 other group projects using GitHub. (Not the best at visuals on websites :/) 

Khanh Doan: I am one year into learning programming. I have a good basic understanding of java, html, css, js. I’m open to learning and working on both the front and back end for this assignment.

Aim: I have experience working mostly with C++ and C. I made a few personal projects involving gamedev. I have been recently exploring html, java and css before the class started. I like solving logical problems or overcoming programming challenges. I am interested in learning as much backend dev as I can.

Han: I have experience in HTML, JS, React web dev but new to Java and Spring Framework! I mostly work on the front-en :)

Leo: I have experience with HTML CSS Javascript SQL database stuff, and most common programming languages, C C++ Java Python Rust. Experiences in some small web server project, generally some experience in fullstack. 

## What is the name of your web application?
Potential app names (to be finalized with the client in the near future):
HahaHealth: Where Laughter and Fitness Collide! 
Gym Friends (with a cute logo of bunch of seals(seals look like family))
SweatSync
ShapeUp

## Do we have a clear understanding of the problem?
Trainer (Client) wants the ability to track their clients, prescribe workouts, and monitor their progress. Easy access to her clients to sign up, and for the trainer to observe their progress, and other similar functionality to ease her workflow. Clients can be grouped into groups/cohorts where they can interact. The trainer can manage multiple groups/cohorts.

## How is this problem solved currently (if at all)?
Excercise.com: Similar idea but our approach will be more trainer-oriented.
Use of different multiple platforms, for example, using Whatsapp for communication, Google Calendar for scheduling, Notes app to take notes and keeping track

## How will this project make life better? Is it educational or just for entertainment?
This will make her life easier, allow her clients to get educated on more workouts etc;
Improve efficiency, save costs related to finding personal workouts, increase community engagement(?)
Encourages people to track their training and progress (helpful for making consistent fitness progress).

## Target audience:
Fitness trainers and their clients, more specifically adult clients with busy lifestyles (parents, young professionals).

## Scope of the project:
An iOS-friendly web-app that allows trainers to manage their client base giving them workouts, and tracking their progress over time. Our application will ease trainers' minds, and allow them 24/7 access to all their client's details, and workouts to further elevate their training. The application is very client-centered, giving them a safe space to get valuable fitness knowledge, and access to free videos and personalized coaching from their trainer.

## Competitive Analysis/Comparison with Alternatives:
	Our app is:
More community-driven, 
More engaging, especially for people who don’t usually work out, casual people,
Focused on building consistent fitness habits over specific results.

## Main features (Epics):
Trainer and clients management: Helps trainers manage all their clients information efficiently. Trainers have access to clients’ progress, allowing them to give feedback and prescribe personalized workout plans.
Workout prescription and tracking: easy and convenient assigning and accessing of personalized training programs for clients. Clients’ progress data over time is tracked, analyzed and visualized.
Community engagement and gamification: forum-style user interactions enabling local communities of users to engage in friendly competition through a system of rewards, rankings, weekly competitions, etc. This makes working out a lot more engaging and enjoyable for a lot of people who otherwise would not usually work out.

## What are some sample stories/scenarios? 
	The users of the app are: a trainer using the app to communicate with their client. Clients who are using the app to assist them in their workout. 
The user logs in and looks at the training program their trainer has designed for them before the workout. They then watch tutorial videos for each of the exercises, perform and then log the exercises for the day. They upload a video of their workout. The trainer later logs in and looks at their clients’ workout logs and videos then provides feedback. Other clients using the app see their workout and congratulate them on hitting a new pr (personal record), the client is happy and feels motivated to train even harder next time.
A busy adult has a predetermined workout. Unfortunately they are asked to cover a shift during their workout session. User decides to stay at work instead of working out. The workout app instead of pressuring people to workout can offer alternative solutions with the goal of keeping a user in the schedule, like offering to workout on the next day instead or offer a 10 minute simple exercise so the user does not feel discouraged from continuing their body improvement journey. Often trainees quit after missing multiple workouts and feel discouraged from continuing working out.

## High-level description of features: 
Login:  Main page for users to login (hashify api/Auth0 for security)
Video recording/Camera: will record videos/photos of clients’ workout and post it in a local ‘forum’
Tracking:  timers, using Google Maps API to track walking distance
Manual inputs: simple number scroller to quickly input workout data
Timer with Instructions: Simple timer with workout instructions, if user prematurely stops workout or pauses, the data is automatically recorded
Analytics and reporting: system collects data and summarises noticeable trends to both users and trainer, trainer can see raw data through request,
Gamified competitive system: each client is assigned a score, each score cutoff is associated with specific rewards such as titles-medals.
Community and Socials: clients can get to know each other in small communities and groups, participate in forums, clients can also host community events/local competitions with ability for any participant to donate to a local prize pool.

## GitHub Repo: 
cmpt276-term-project (github.com)
