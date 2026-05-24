# claude-jail

This is where I define where I run Claude Code (hint: in a VM).

Claude Code has read access to the whole machine by default, and I don't want it to have read access to my machine, so I need to run it on another machine. So I make one.

Fortunately, Claude Code is known for its small footprint, so I can create a small machine.

## Usage

Install [direnv] and [nix], `direnv allow` the project, then:

```
bb tasks
```

[direnv]: https://direnv.net
[nix]: https://nixos.org

# License

This project is mostly meant for my own personal use, but if you find it useful, feel free to fork. To make that easier, I am publishing this under [0BSD].

[0BSD]: https://opensource.org/license/0bsd
