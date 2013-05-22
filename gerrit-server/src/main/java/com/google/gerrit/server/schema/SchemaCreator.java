begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|AccessSection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|LabelType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|LabelValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|Permission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|PermissionRule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|CurrentSchemaVersion
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
operator|.
name|InheritableBoolean
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|SystemConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|GerritPersonIdent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|GroupUUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|AllProjectsName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePath
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|GitRepositoryManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|MetaDataUpdate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|ProjectConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcExecutor
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|jdbc
operator|.
name|JdbcSchema
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|RefUpdate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_comment
comment|/** Creates the current database schema and populates initial code rows. */
end_comment

begin_class
DECL|class|SchemaCreator
specifier|public
class|class
name|SchemaCreator
block|{
specifier|private
specifier|final
annotation|@
name|SitePath
DECL|field|site_path
name|File
name|site_path
decl_stmt|;
DECL|field|mgr
specifier|private
specifier|final
name|GitRepositoryManager
name|mgr
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|serverUser
specifier|private
specifier|final
name|PersonIdent
name|serverUser
decl_stmt|;
DECL|field|dataSourceType
specifier|private
specifier|final
name|DataSourceType
name|dataSourceType
decl_stmt|;
DECL|field|versionNbr
specifier|private
specifier|final
name|int
name|versionNbr
decl_stmt|;
DECL|field|admin
specifier|private
name|AccountGroup
name|admin
decl_stmt|;
DECL|field|anonymous
specifier|private
name|AccountGroup
name|anonymous
decl_stmt|;
DECL|field|registered
specifier|private
name|AccountGroup
name|registered
decl_stmt|;
DECL|field|owners
specifier|private
name|AccountGroup
name|owners
decl_stmt|;
annotation|@
name|Inject
DECL|method|SchemaCreator (SitePaths site, @Current SchemaVersion version, GitRepositoryManager mgr, AllProjectsName allProjectsName, @GerritPersonIdent PersonIdent au, DataSourceType dst)
specifier|public
name|SchemaCreator
parameter_list|(
name|SitePaths
name|site
parameter_list|,
annotation|@
name|Current
name|SchemaVersion
name|version
parameter_list|,
name|GitRepositoryManager
name|mgr
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|au
parameter_list|,
name|DataSourceType
name|dst
parameter_list|)
block|{
name|this
argument_list|(
name|site
operator|.
name|site_path
argument_list|,
name|version
argument_list|,
name|mgr
argument_list|,
name|allProjectsName
argument_list|,
name|au
argument_list|,
name|dst
argument_list|)
expr_stmt|;
block|}
DECL|method|SchemaCreator (@itePath File site, @Current SchemaVersion version, GitRepositoryManager gitMgr, AllProjectsName ap, @GerritPersonIdent PersonIdent au, DataSourceType dst)
specifier|public
name|SchemaCreator
parameter_list|(
annotation|@
name|SitePath
name|File
name|site
parameter_list|,
annotation|@
name|Current
name|SchemaVersion
name|version
parameter_list|,
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|AllProjectsName
name|ap
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|au
parameter_list|,
name|DataSourceType
name|dst
parameter_list|)
block|{
name|site_path
operator|=
name|site
expr_stmt|;
name|mgr
operator|=
name|gitMgr
expr_stmt|;
name|allProjectsName
operator|=
name|ap
expr_stmt|;
name|serverUser
operator|=
name|au
expr_stmt|;
name|dataSourceType
operator|=
name|dst
expr_stmt|;
name|versionNbr
operator|=
name|version
operator|.
name|getVersionNbr
argument_list|()
expr_stmt|;
block|}
DECL|method|create (final ReviewDb db)
specifier|public
name|void
name|create
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
specifier|final
name|JdbcSchema
name|jdbc
init|=
operator|(
name|JdbcSchema
operator|)
name|db
decl_stmt|;
specifier|final
name|JdbcExecutor
name|e
init|=
operator|new
name|JdbcExecutor
argument_list|(
name|jdbc
argument_list|)
decl_stmt|;
try|try
block|{
name|jdbc
operator|.
name|updateSchema
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|e
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|final
name|CurrentSchemaVersion
name|sVer
init|=
name|CurrentSchemaVersion
operator|.
name|create
argument_list|()
decl_stmt|;
name|sVer
operator|.
name|versionNbr
operator|=
name|versionNbr
expr_stmt|;
name|db
operator|.
name|schemaVersion
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|sVer
argument_list|)
argument_list|)
expr_stmt|;
name|initSystemConfig
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|initAllProjects
argument_list|()
expr_stmt|;
name|dataSourceType
operator|.
name|getIndexScript
argument_list|()
operator|.
name|run
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
DECL|method|newGroup (ReviewDb c, String name, AccountGroup.UUID uuid)
specifier|private
name|AccountGroup
name|newGroup
parameter_list|(
name|ReviewDb
name|c
parameter_list|,
name|String
name|name
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|uuid
operator|==
literal|null
condition|)
block|{
name|uuid
operator|=
name|GroupUUID
operator|.
name|make
argument_list|(
name|name
argument_list|,
name|serverUser
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|AccountGroup
argument_list|(
comment|//
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
argument_list|,
comment|//
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|c
operator|.
name|nextAccountGroupId
argument_list|()
argument_list|)
argument_list|,
comment|//
name|uuid
argument_list|)
return|;
block|}
DECL|method|initSystemConfig (final ReviewDb c)
specifier|private
name|SystemConfig
name|initSystemConfig
parameter_list|(
specifier|final
name|ReviewDb
name|c
parameter_list|)
throws|throws
name|OrmException
block|{
name|admin
operator|=
name|newGroup
argument_list|(
name|c
argument_list|,
literal|"Administrators"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|admin
operator|.
name|setDescription
argument_list|(
literal|"Gerrit Site Administrators"
argument_list|)
expr_stmt|;
name|admin
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|admin
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|admin
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|anonymous
operator|=
name|newGroup
argument_list|(
name|c
argument_list|,
literal|"Anonymous Users"
argument_list|,
name|AccountGroup
operator|.
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
name|anonymous
operator|.
name|setDescription
argument_list|(
literal|"Any user, signed-in or not"
argument_list|)
expr_stmt|;
name|anonymous
operator|.
name|setOwnerGroupUUID
argument_list|(
name|admin
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
name|anonymous
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|anonymous
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|anonymous
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|registered
operator|=
name|newGroup
argument_list|(
name|c
argument_list|,
literal|"Registered Users"
argument_list|,
name|AccountGroup
operator|.
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|registered
operator|.
name|setDescription
argument_list|(
literal|"Any signed-in user"
argument_list|)
expr_stmt|;
name|registered
operator|.
name|setOwnerGroupUUID
argument_list|(
name|admin
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
name|registered
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|registered
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|registered
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|AccountGroup
name|batchUsers
init|=
name|newGroup
argument_list|(
name|c
argument_list|,
literal|"Non-Interactive Users"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|batchUsers
operator|.
name|setDescription
argument_list|(
literal|"Users who perform batch actions on Gerrit"
argument_list|)
expr_stmt|;
name|batchUsers
operator|.
name|setOwnerGroupUUID
argument_list|(
name|admin
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
name|batchUsers
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|INTERNAL
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|batchUsers
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|batchUsers
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|owners
operator|=
name|newGroup
argument_list|(
name|c
argument_list|,
literal|"Project Owners"
argument_list|,
name|AccountGroup
operator|.
name|PROJECT_OWNERS
argument_list|)
expr_stmt|;
name|owners
operator|.
name|setDescription
argument_list|(
literal|"Any owner of the project"
argument_list|)
expr_stmt|;
name|owners
operator|.
name|setOwnerGroupUUID
argument_list|(
name|admin
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
name|owners
operator|.
name|setType
argument_list|(
name|AccountGroup
operator|.
name|Type
operator|.
name|SYSTEM
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroups
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|owners
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|accountGroupNames
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|AccountGroupName
argument_list|(
name|owners
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|SystemConfig
name|s
init|=
name|SystemConfig
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|s
operator|.
name|sitePath
operator|=
name|site_path
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|s
operator|.
name|sitePath
operator|=
name|site_path
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
name|c
operator|.
name|systemConfig
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|s
return|;
block|}
DECL|method|initAllProjects ()
specifier|private
name|void
name|initAllProjects
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|Repository
name|git
init|=
literal|null
decl_stmt|;
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|openRepository
argument_list|(
name|allProjectsName
argument_list|)
expr_stmt|;
name|initAllProjects
argument_list|(
name|git
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
comment|// A repository may be missing if this project existed only to store
comment|// inheritable permissions. For example 'All-Projects'.
try|try
block|{
name|git
operator|=
name|mgr
operator|.
name|createRepository
argument_list|(
name|allProjectsName
argument_list|)
expr_stmt|;
name|initAllProjects
argument_list|(
name|git
argument_list|)
expr_stmt|;
specifier|final
name|RefUpdate
name|u
init|=
name|git
operator|.
name|updateRef
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
decl_stmt|;
name|u
operator|.
name|link
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|err
parameter_list|)
block|{
specifier|final
name|String
name|name
init|=
name|allProjectsName
operator|.
name|get
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create repository "
operator|+
name|name
argument_list|,
name|err
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|git
operator|!=
literal|null
condition|)
block|{
name|git
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|initAllProjects (Repository git)
specifier|private
name|void
name|initAllProjects
parameter_list|(
name|Repository
name|git
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
operator|new
name|MetaDataUpdate
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
argument_list|,
name|allProjectsName
argument_list|,
name|git
argument_list|)
decl_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setAuthor
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|md
operator|.
name|getCommitBuilder
argument_list|()
operator|.
name|setCommitter
argument_list|(
name|serverUser
argument_list|)
expr_stmt|;
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Project
name|p
init|=
name|config
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|p
operator|.
name|setDescription
argument_list|(
literal|"Access inherited by all other projects."
argument_list|)
expr_stmt|;
name|p
operator|.
name|setRequireChangeID
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseContentMerge
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseSignedOffBy
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|AccessSection
name|cap
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|all
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|ALL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|heads
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|HEADS
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|tags
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/tags/*"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|meta
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AccessSection
name|magic
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/for/"
operator|+
name|AccessSection
operator|.
name|ALL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|cap
argument_list|,
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|all
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|admin
argument_list|,
name|anonymous
argument_list|)
expr_stmt|;
name|LabelType
name|cr
init|=
name|initCodeReviewLabel
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|cr
argument_list|,
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|cr
argument_list|,
operator|-
literal|2
argument_list|,
literal|2
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|FORGE_AUTHOR
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|FORGE_COMMITTER
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|heads
argument_list|,
name|Permission
operator|.
name|EDIT_TOPIC_NAME
argument_list|,
literal|true
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|tags
argument_list|,
name|Permission
operator|.
name|PUSH_TAG
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|tags
argument_list|,
name|Permission
operator|.
name|PUSH_SIGNED_TAG
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|magic
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|magic
argument_list|,
name|Permission
operator|.
name|PUSH_MERGE
argument_list|,
name|registered
argument_list|)
expr_stmt|;
name|meta
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|READ
argument_list|,
literal|true
argument_list|)
operator|.
name|setExclusiveGroup
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|cr
argument_list|,
operator|-
literal|2
argument_list|,
literal|2
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|config
argument_list|,
name|meta
argument_list|,
name|Permission
operator|.
name|SUBMIT
argument_list|,
name|admin
argument_list|,
name|owners
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
literal|"Initialized Gerrit Code Review "
operator|+
name|Version
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, String permission, AccountGroup group1, AccountGroup... groupList)
specifier|private
name|PermissionRule
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|String
name|permission
parameter_list|,
name|AccountGroup
name|group1
parameter_list|,
name|AccountGroup
modifier|...
name|groupList
parameter_list|)
block|{
return|return
name|grant
argument_list|(
name|config
argument_list|,
name|section
argument_list|,
name|permission
argument_list|,
literal|false
argument_list|,
name|group1
argument_list|,
name|groupList
argument_list|)
return|;
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, String permission, boolean force, AccountGroup group1, AccountGroup... groupList)
specifier|private
name|PermissionRule
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|String
name|permission
parameter_list|,
name|boolean
name|force
parameter_list|,
name|AccountGroup
name|group1
parameter_list|,
name|AccountGroup
modifier|...
name|groupList
parameter_list|)
block|{
name|Permission
name|p
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|permission
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PermissionRule
name|rule
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|group1
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setForce
argument_list|(
name|force
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroup
name|group
range|:
name|groupList
control|)
block|{
name|rule
operator|=
name|rule
argument_list|(
name|config
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|rule
operator|.
name|setForce
argument_list|(
name|force
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
return|return
name|rule
return|;
block|}
DECL|method|grant (ProjectConfig config, AccessSection section, LabelType type, int min, int max, AccountGroup... groupList)
specifier|private
name|void
name|grant
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|LabelType
name|type
parameter_list|,
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|,
name|AccountGroup
modifier|...
name|groupList
parameter_list|)
block|{
name|String
name|name
init|=
name|Permission
operator|.
name|LABEL
operator|+
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Permission
name|p
init|=
name|section
operator|.
name|getPermission
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroup
name|group
range|:
name|groupList
control|)
block|{
name|PermissionRule
name|r
init|=
name|rule
argument_list|(
name|config
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|r
operator|.
name|setRange
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|rule (ProjectConfig config, AccountGroup group)
specifier|private
name|PermissionRule
name|rule
parameter_list|(
name|ProjectConfig
name|config
parameter_list|,
name|AccountGroup
name|group
parameter_list|)
block|{
return|return
operator|new
name|PermissionRule
argument_list|(
name|config
operator|.
name|resolve
argument_list|(
name|group
argument_list|)
argument_list|)
return|;
block|}
DECL|method|initCodeReviewLabel (ProjectConfig c)
specifier|public
specifier|static
name|LabelType
name|initCodeReviewLabel
parameter_list|(
name|ProjectConfig
name|c
parameter_list|)
block|{
name|LabelType
name|type
init|=
operator|new
name|LabelType
argument_list|(
literal|"Code-Review"
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
literal|"I would prefer that you didn't submit this"
argument_list|)
argument_list|,
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|2
argument_list|,
literal|"Do not submit"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|type
operator|.
name|setAbbreviation
argument_list|(
literal|"CR"
argument_list|)
expr_stmt|;
name|type
operator|.
name|setCopyMinScore
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|c
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|type
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

