# findex

## Why/What

- we are all born messy, so are our files stored on many places
- multiple computers, NASs, external hard drives, memory cards, USB keys, ...
- most de-duplication tools do not help us audit/verify on content, they focus
on comparing folder structures (ideally identical) and not the files themselves.

For example, me with a digital camera:

- pictures are stored without structure on SD card when you take them
- you copy them on your hard drive when you have time, sometimes, on good days, not so often.
- before going on a trip, you worry the card may get full, so you may make an extra copy of the whole SD card on an external drive "just in case".
- you might worry to have forgotten some pictures, so you make a quick copy on your computer desktop in a "TODO" folder
- your computer may die, so you make backups, the backup fails at 99% after taking 2 days because the drive/network is slow or unreliable
- you have no idea if the 1% missing part of the backup is relevant or not
- to store your backups and files, you might have a NAS with terabytes of data close to capacity and another with plenty of free space, moving all of that reliably with rsync or other tools will take weeks because one of them is slow.
- to make things worse, you might have different folder structures, when in fact you mostly care about the files and their content, not how things are organized.

If you are like me, then you probably start to have trust issues in your (lack of) process to store and organize your files,
and you start to wonder if we could have a tool that help to organize things without loosing data.

## How it works

Findex recursively reads all files in a folder and store their hash in a text-based index stored in `.findex` file at the root.
The tool to write the index is a simple Bash script for portability and assumes that reading all files in folder is relatively fast when running on a local filesystem.

With an `.findex` file, you can then:
- get a summary of the folder that was indexed: total size, amount of duplication (in file count and bytes)
- compare two indexes and get a list of files that are only contained in one but not the other

## Use-cases

- backup: verify backup restore contains all the expected files, if not identify the missing files
- organize: find and report duplicated files and help reduce duplication
- help handle files in temporary/transient locations: sd card, removable storage, temp folder (single copy)
- help with data spread on multiple hosts: computing index on each host, then comparing the indices without copy any data across network
