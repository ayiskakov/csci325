**Output for TxtTextSearch** 
```text
❯ java TxtTextSearch.java ./txts "many branches of mathematics"
algebra.txt: 1
biology.txt: 0
❯ java TxtTextSearch.java ./txts "many branches mathematics"
algebra.txt: 17
biology.txt: 0
❯ java TxtTextSearch.java ./txts "many branches mathematics geometry"
algebra.txt: 21
biology.txt: 0
```

run with
```bash 
cd lab2/task_2 && java TxtTextSearch.java ./txts "many branches of mathematics"
```


