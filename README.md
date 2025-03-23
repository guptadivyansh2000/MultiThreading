Producer-Consumer Multi-Threading Solution

Description
This project implements a multi-threading solution for the classic Producer-Consumer problem.

The producer continuously produces stock as long as the stock in hand (SiH) is below the Producer Total Capacity (PTC).

Ten consumers request stock from the producer, reducing the SiH accordingly. Each consumer has its own capacity and threshold limits.

The process runs indefinitely until manually stopped, with random delays introduced to simulate production and sales times.

Features

Producer: Produces stock until reaching its total capacity.
 
Consumers: Ten consumers request and reduce stock, each with individual capacity and threshold limits.

Synchronization: Ensures proper synchronization and inter-thread communication.

Random Delays: Simulates production and sales times with random delays.

Usage

Clone the repository.
Open the project in IntelliJ IDEA.
Run the main class to start the simulation.
Monitor the console output for producer and consumer activities.
