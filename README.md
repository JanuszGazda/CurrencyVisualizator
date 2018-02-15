# CurrencyVisualizator
Java, Swing, PostgreSQL application for visualizing currency values over time.
Current values are being downloaded from nbp.pl stored in DB and visualized with usage of JFreeChart library.

To run project, import it to InteliJ and be sure to have PostgreSQL DB.
There must be a schema 'public', tables will create itself if don't exist.
In class ConnectToPostgres change DB configuration.

Application not yet resistant to all possible errors.
