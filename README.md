

### Useful commands

#### Run the examples server

(WIP)


To continuously run the server and recompile it on changes:
```shell
$ mill --watch webApp.backend.runBackground
```
#### How to access it

Open http://localhost:8090/ in your browser.


### References

- Prototype based on [this POC](https://github.com/ajnsit/purescript-concur-streaming-poc)
- A fleshed out, [callback version](https://github.com/ajbarber/purescript-concur-core/blob/c66c3a9f8e7e325e86ab5faa1505aa2e51a46e4b/src/Concur/Core/Types.purs)
  of purescript-concur