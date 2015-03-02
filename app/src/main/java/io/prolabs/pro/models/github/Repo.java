package io.prolabs.pro.models.github;

import java.security.Permissions;
import java.security.acl.Owner;
import java.util.HashMap;
import java.util.Map;

public class Repo {

    private int id;
    private Owner owner;
    private String name;
    private String fullName;
    private String description;
    private boolean _private;
    private boolean fork;
    private String url;
    private String htmlUrl;
    private String cloneUrl;
    private String gitUrl;
    private String sshUrl;
    private String svnUrl;
    private String mirrorUrl;
    private String homepage;
    private Object language;
    private int forksCount;
    private int stargazersCount;
    private int watchersCount;
    private int size;
    private String defaultBranch;
    private int openIssuesCount;
    private boolean hasIssues;
    private boolean hasWiki;
    private boolean hasPages;
    private boolean hasDownloads;
    private String pushedAt;
    private String createdAt;
    private String updatedAt;
    private Permissions permissions;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * @param owner The owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The _private
     */
    public boolean isPrivate() {
        return _private;
    }

    /**
     * @param _private The private
     */
    public void setPrivate(boolean _private) {
        this._private = _private;
    }

    /**
     * @return The fork
     */
    public boolean isFork() {
        return fork;
    }

    /**
     * @param fork The fork
     */
    public void setFork(boolean fork) {
        this.fork = fork;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl The html_url
     */
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
     * @return The cloneUrl
     */
    public String getCloneUrl() {
        return cloneUrl;
    }

    /**
     * @param cloneUrl The clone_url
     */
    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    /**
     * @return The gitUrl
     */
    public String getGitUrl() {
        return gitUrl;
    }

    /**
     * @param gitUrl The git_url
     */
    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    /**
     * @return The sshUrl
     */
    public String getSshUrl() {
        return sshUrl;
    }

    /**
     * @param sshUrl The ssh_url
     */
    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    /**
     * @return The svnUrl
     */
    public String getSvnUrl() {
        return svnUrl;
    }

    /**
     * @param svnUrl The svn_url
     */
    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    /**
     * @return The mirrorUrl
     */
    public String getMirrorUrl() {
        return mirrorUrl;
    }

    /**
     * @param mirrorUrl The mirror_url
     */
    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    /**
     * @return The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return The language
     */
    public Object getLanguage() {
        return language;
    }

    /**
     * @param language The language
     */
    public void setLanguage(Object language) {
        this.language = language;
    }

    /**
     * @return The forksCount
     */
    public int getForksCount() {
        return forksCount;
    }

    /**
     * @param forksCount The forks_count
     */
    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    /**
     * @return The stargazersCount
     */
    public int getStargazersCount() {
        return stargazersCount;
    }

    /**
     * @param stargazersCount The stargazers_count
     */
    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    /**
     * @return The watchersCount
     */
    public int getWatchersCount() {
        return watchersCount;
    }

    /**
     * @param watchersCount The watchers_count
     */
    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    /**
     * @return The size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size The size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return The defaultBranch
     */
    public String getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * @param defaultBranch The default_branch
     */
    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    /**
     * @return The openIssuesCount
     */
    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    /**
     * @param openIssuesCount The open_issues_count
     */
    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    /**
     * @return The hasIssues
     */
    public boolean isHasIssues() {
        return hasIssues;
    }

    /**
     * @param hasIssues The has_issues
     */
    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    /**
     * @return The hasWiki
     */
    public boolean isHasWiki() {
        return hasWiki;
    }

    /**
     * @param hasWiki The has_wiki
     */
    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    /**
     * @return The hasPages
     */
    public boolean isHasPages() {
        return hasPages;
    }

    /**
     * @param hasPages The has_pages
     */
    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    /**
     * @return The hasDownloads
     */
    public boolean isHasDownloads() {
        return hasDownloads;
    }

    /**
     * @param hasDownloads The has_downloads
     */
    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    /**
     * @return The pushedAt
     */
    public String getPushedAt() {
        return pushedAt;
    }

    /**
     * @param pushedAt The pushed_at
     */
    public void setPushedAt(String pushedAt) {
        this.pushedAt = pushedAt;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The permissions
     */
    public Permissions getPermissions() {
        return permissions;
    }

    /**
     * @param permissions The permissions
     */
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}