Mac Setup instructions

Install Homebrew
```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Use Homebrew to install Java & Maven
```
brew install java
brew install maven
```

Install Docker Desktop
```
https://www.docker.com/products/docker-desktop/
```

compose up the dev environment
```
docker-compose up -d --build
```

Optional Steps

1. Install Docker for VS Code
```
https://code.visualstudio.com/docs/containers/overview
```
2. Install Warp the terminal of the future
```
https://www.warp.dev/
```

To run the QA environment locally you must add the following entries to your hosts file;
```
127.0.0.1 inventory.hiro.world
127.0.0.1 api.inventory.hiro.world
```

then run the following command
```
docker-compose -f docker-compose-qa.yml up -d --build
```

If you are running the QA environment on a dedicated server add an entry into your dns resolver to map the following to the correct IP;
```
inventory.hiro.world
api.inventory.hiro.world
```
