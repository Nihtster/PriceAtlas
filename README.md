# VS Code Readme Editor 📝

Import your existing Readme using the import button at the bottom,
or create a new Readme from scratch by typing in the editor.

## Get Started 🚀

To get started, hit the 'clear' button at the top of the editor!

## Prebuilt Components/Templates 🔥

You can checkout prebuilt components and templates by clicking on the 'Add Section' button or menu icon
on the top left corner of the navbar.

## Save Readme ✨

Once you're done, click on the save button to download and save your ReadMe!

# PriceAtlas

A CLI application built for tracking the prices
of in-game items (in-game and real currency)
for various video games.

## Dependencies

This application requires the use of MySQL community server and assumes
that the user understands how to interact with the MySQL client. If not,
please follow the official instructions for setting up MySQL on your system [here](https://dev.mysql.com/doc/refman/8.2/en/installing.html).

## Setting up the MySQL Database

Launch the MySQL command line client and login

```bash
mysql -u [username] -p [hit enter to be prompted for password entry]
```

Copy the creation scripts from

Copy the date insertion scripts at the bottom of this document into the command line after the creation scripts have completed.

## Run Locally

Clone the project

```bash
  git clone https://github.com/Nihtster/PriceAtlas
```

Go to the project directory

```bash
  cd PriceAtlas
```

compile the java code

```bash
javac PriceAtlas.java
```

Start the application

If you installed the connector elsewhere, run this line:

```bash
java -cp .;path/to/my-sql-connector-j-x.x.x.jar PriceAtlas
```

If you are using the connector in the repository, run this line:

```bash
java -cp .;lib/my-sql-connector-j-x.x.x.jar PriceAtlas
```

replace the 'x' with the appropriate version numbers.

## Using the application

The application will prompt you for your:

- DB url (i.e. jdbc:mysql://localhost:3306/PRICEATLAS)
- username (i.e. root)
- password (i.e. MyPassword1234)
- JDBC driver package (i.e. com.mysql.cj.jdbc.Driver)

## Creators

|                                                                       Author                                                                       |                                                                    Collaborator                                                                     |                                                                         Collaborator                                                                         |                                                                                                                                               Collaborator |
| :------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------------------------------------: | ---------------------------------------------------------------------------------------------------------------------------------------------------------: |
| [<img src="https://avatars.githubusercontent.com/u/77190903?v=4size=115" width=115><br><sub>@Nihtster</sub>](https://github.com/Nihtster) <br><br> | [<img src="https://avatars.githubusercontent.com/u/42759927?size=255" width=115><br><sub>@Nathmon565</sub>](https://github.com/Nathmon565) <br><br> | [<img src="https://avatars.githubusercontent.com/u/142537220?v=4size=255" width=115><br><sub>@Tristan Breen</sub>](https://github.com/TristanBreen) <br><br> | [<img src="https://avatars.githubusercontent.com/u/38901938?v=4?size=255" width=115><br><sub>@Xander Treat</sub>](https://github.com/MisterX2003) <br><br> |

## Contributing

Contributions are always welcome!

See `contributing.md` for ways to get started.

Please adhere to this project's `code of conduct`.

## License

[MIT](https://choosealicense.com/licenses/mit/)
