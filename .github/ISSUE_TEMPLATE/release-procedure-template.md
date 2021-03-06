---
name: Release procedure template
about: A template for the release procedure of each release.
title: ''
labels: ''
assignees: ''

---

- [ ] Make sure the automated tests pass. Check Jenkins (https://jenkins-master.sync.ro/job/oxygen-git-plugin/).
- [ ] Add the "What's New" list to **addon.xml**.
- [ ] When ready for release, start a Jenkins build providing the release version and selecting the RELEASE check box.
- [ ] Add the "What's New" list to the new release from GitHub. See https://github.com/oxygenxml/oxygen-git-plugin/releases.
- [ ] Check the new version is available on the default update site.
- [ ] Send e-mail to the users who agreed to be notified when what they requested has been implemented.
- [ ] Update the forum threads where users requested things that have been implemented for the current release.
- [ ] Promote the release on social media, oxygen-users list and DITA list.
- [ ] Update internal version on Jenkins.
