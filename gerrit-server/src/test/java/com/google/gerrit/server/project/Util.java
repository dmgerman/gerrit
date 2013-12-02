begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|CacheBuilder
import|;
end_import

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
name|Lists
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
name|GroupReference
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
name|AccountProjectWatch
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
name|Change
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
name|NameKey
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
name|rules
operator|.
name|PrologEnvironment
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
name|rules
operator|.
name|RulesCache
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
name|CurrentUser
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
name|CapabilityControl
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
name|GroupMembership
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
name|ListGroupMembership
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
name|FactoryModule
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
name|GerritServerConfig
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
name|ProjectConfig
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
name|Guice
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
name|Injector
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
name|Config
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|Util
specifier|public
class|class
name|Util
block|{
DECL|field|ADMIN
specifier|public
specifier|static
name|AccountGroup
operator|.
name|UUID
name|ADMIN
init|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"test.admin"
argument_list|)
decl_stmt|;
DECL|field|DEVS
specifier|public
specifier|static
name|AccountGroup
operator|.
name|UUID
name|DEVS
init|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"test.devs"
argument_list|)
decl_stmt|;
DECL|field|CR
specifier|public
specifier|static
specifier|final
name|LabelType
name|CR
init|=
name|category
argument_list|(
literal|"Code-Review"
argument_list|,
name|value
argument_list|(
literal|2
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"I would prefer that you didn't submit this"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|2
argument_list|,
literal|"Do not submit"
argument_list|)
argument_list|)
decl_stmt|;
DECL|method|value (int value, String text)
specifier|public
specifier|static
name|LabelValue
name|value
parameter_list|(
name|int
name|value
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
name|value
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|category (String name, LabelValue... values)
specifier|public
specifier|static
name|LabelType
name|category
parameter_list|(
name|String
name|name
parameter_list|,
name|LabelValue
modifier|...
name|values
parameter_list|)
block|{
return|return
operator|new
name|LabelType
argument_list|(
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newRule (ProjectConfig project, AccountGroup.UUID groupUUID)
specifier|static
specifier|public
name|PermissionRule
name|newRule
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|)
block|{
name|GroupReference
name|group
init|=
operator|new
name|GroupReference
argument_list|(
name|groupUUID
argument_list|,
name|groupUUID
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|group
operator|=
name|project
operator|.
name|resolve
argument_list|(
name|group
argument_list|)
expr_stmt|;
return|return
operator|new
name|PermissionRule
argument_list|(
name|group
argument_list|)
return|;
block|}
DECL|method|grant (ProjectConfig project, String permissionName, int min, int max, AccountGroup.UUID group, String ref)
specifier|static
specifier|public
name|PermissionRule
name|grant
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|String
name|permissionName
parameter_list|,
name|int
name|min
parameter_list|,
name|int
name|max
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|PermissionRule
name|rule
init|=
name|newRule
argument_list|(
name|project
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setMin
argument_list|(
name|min
argument_list|)
expr_stmt|;
name|rule
operator|.
name|setMax
argument_list|(
name|max
argument_list|)
expr_stmt|;
return|return
name|grant
argument_list|(
name|project
argument_list|,
name|permissionName
argument_list|,
name|rule
argument_list|,
name|ref
argument_list|)
return|;
block|}
DECL|method|grant (ProjectConfig project, String permissionName, AccountGroup.UUID group, String ref)
specifier|static
specifier|public
name|PermissionRule
name|grant
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|String
name|permissionName
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
return|return
name|grant
argument_list|(
name|project
argument_list|,
name|permissionName
argument_list|,
name|newRule
argument_list|(
name|project
argument_list|,
name|group
argument_list|)
argument_list|,
name|ref
argument_list|)
return|;
block|}
DECL|method|doNotInherit (ProjectConfig project, String permissionName, String ref)
specifier|static
specifier|public
name|void
name|doNotInherit
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|String
name|permissionName
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|project
operator|.
name|getAccessSection
argument_list|(
name|ref
argument_list|,
literal|true
argument_list|)
comment|//
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|,
literal|true
argument_list|)
comment|//
operator|.
name|setExclusiveGroup
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|grant (ProjectConfig project, String permissionName, PermissionRule rule, String ref)
specifier|static
specifier|private
name|PermissionRule
name|grant
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|String
name|permissionName
parameter_list|,
name|PermissionRule
name|rule
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|project
operator|.
name|getAccessSection
argument_list|(
name|ref
argument_list|,
literal|true
argument_list|)
comment|//
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|,
literal|true
argument_list|)
comment|//
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
return|return
name|rule
return|;
block|}
DECL|field|all
specifier|private
specifier|final
name|Map
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectState
argument_list|>
name|all
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|capabilityControlFactory
specifier|private
specifier|final
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
decl_stmt|;
DECL|field|sectionSorter
specifier|private
specifier|final
name|PermissionCollection
operator|.
name|Factory
name|sectionSorter
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
init|=
operator|new
name|AllProjectsName
argument_list|(
literal|"parent"
argument_list|)
decl_stmt|;
DECL|field|parent
specifier|private
specifier|final
name|ProjectConfig
name|parent
init|=
operator|new
name|ProjectConfig
argument_list|(
name|allProjectsName
argument_list|)
decl_stmt|;
DECL|method|Util ()
specifier|public
name|Util
parameter_list|()
block|{
name|all
operator|=
operator|new
name|HashMap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectState
argument_list|>
argument_list|()
expr_stmt|;
name|parent
operator|.
name|createInMemory
argument_list|()
expr_stmt|;
name|parent
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|CR
operator|.
name|getName
argument_list|()
argument_list|,
name|CR
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|projectCache
operator|=
operator|new
name|ProjectCache
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ProjectState
name|getAllProjects
parameter_list|()
block|{
return|return
name|get
argument_list|(
name|allProjectsName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProjectState
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
block|{
return|return
name|all
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|evict
parameter_list|(
name|Project
name|p
parameter_list|)
block|{       }
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|(
name|Project
name|p
parameter_list|)
block|{       }
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|byName
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onCreateProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|newProjectName
parameter_list|)
block|{       }
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|guessRelevantGroupUUIDs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProjectState
name|checkedGet
parameter_list|(
name|NameKey
name|projectName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|all
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|evict
parameter_list|(
name|NameKey
name|p
parameter_list|)
block|{       }
block|}
expr_stmt|;
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|FactoryModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|Config
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|GerritServerConfig
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|Config
argument_list|()
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|CapabilityControl
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ProjectCache
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|projectCache
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Cache
argument_list|<
name|SectionSortCache
operator|.
name|EntryKey
argument_list|,
name|SectionSortCache
operator|.
name|EntryVal
argument_list|>
name|c
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|sectionSorter
operator|=
operator|new
name|PermissionCollection
operator|.
name|Factory
argument_list|(
operator|new
name|SectionSortCache
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|capabilityControlFactory
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|CapabilityControl
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getParentConfig ()
specifier|public
name|ProjectConfig
name|getParentConfig
parameter_list|()
block|{
return|return
name|this
operator|.
name|parent
return|;
block|}
DECL|method|add (ProjectConfig pc)
specifier|public
name|void
name|add
parameter_list|(
name|ProjectConfig
name|pc
parameter_list|)
block|{
name|PrologEnvironment
operator|.
name|Factory
name|envFactory
init|=
literal|null
decl_stmt|;
name|GitRepositoryManager
name|mgr
init|=
literal|null
decl_stmt|;
name|ProjectControl
operator|.
name|AssistedFactory
name|projectControlFactory
init|=
literal|null
decl_stmt|;
name|RulesCache
name|rulesCache
init|=
literal|null
decl_stmt|;
name|SitePaths
name|sitePaths
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|CommentLinkInfo
argument_list|>
name|commentLinks
init|=
literal|null
decl_stmt|;
name|all
operator|.
name|put
argument_list|(
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
operator|new
name|ProjectState
argument_list|(
name|sitePaths
argument_list|,
name|projectCache
argument_list|,
name|allProjectsName
argument_list|,
name|projectControlFactory
argument_list|,
name|envFactory
argument_list|,
name|mgr
argument_list|,
name|rulesCache
argument_list|,
name|commentLinks
argument_list|,
name|pc
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|user (ProjectConfig local, AccountGroup.UUID... memberOf)
specifier|public
name|ProjectControl
name|user
parameter_list|(
name|ProjectConfig
name|local
parameter_list|,
name|AccountGroup
operator|.
name|UUID
modifier|...
name|memberOf
parameter_list|)
block|{
return|return
name|user
argument_list|(
name|local
argument_list|,
literal|null
argument_list|,
name|memberOf
argument_list|)
return|;
block|}
DECL|method|user (ProjectConfig local, String name, AccountGroup.UUID... memberOf)
specifier|public
name|ProjectControl
name|user
parameter_list|(
name|ProjectConfig
name|local
parameter_list|,
name|String
name|name
parameter_list|,
name|AccountGroup
operator|.
name|UUID
modifier|...
name|memberOf
parameter_list|)
block|{
name|String
name|canonicalWebUrl
init|=
literal|"http://localhost"
decl_stmt|;
return|return
operator|new
name|ProjectControl
argument_list|(
name|Collections
operator|.
expr|<
name|AccountGroup
operator|.
name|UUID
operator|>
name|emptySet
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|AccountGroup
operator|.
name|UUID
operator|>
name|emptySet
argument_list|()
argument_list|,
name|projectCache
argument_list|,
name|sectionSorter
argument_list|,
literal|null
argument_list|,
name|canonicalWebUrl
argument_list|,
operator|new
name|MockUser
argument_list|(
name|name
argument_list|,
name|memberOf
argument_list|)
argument_list|,
name|newProjectState
argument_list|(
name|local
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newProjectState (ProjectConfig local)
specifier|private
name|ProjectState
name|newProjectState
parameter_list|(
name|ProjectConfig
name|local
parameter_list|)
block|{
name|add
argument_list|(
name|local
argument_list|)
expr_stmt|;
return|return
name|all
operator|.
name|get
argument_list|(
name|local
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
return|;
block|}
DECL|class|MockUser
specifier|private
class|class
name|MockUser
extends|extends
name|CurrentUser
block|{
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|groups
specifier|private
specifier|final
name|GroupMembership
name|groups
decl_stmt|;
DECL|method|MockUser (String name, AccountGroup.UUID[] groupId)
name|MockUser
parameter_list|(
name|String
name|name
parameter_list|,
name|AccountGroup
operator|.
name|UUID
index|[]
name|groupId
parameter_list|)
block|{
name|super
argument_list|(
name|capabilityControlFactory
argument_list|)
expr_stmt|;
name|username
operator|=
name|name
expr_stmt|;
name|ArrayList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|groupIds
operator|.
name|add
argument_list|(
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|groupIds
operator|.
name|add
argument_list|(
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
name|groups
operator|=
operator|new
name|ListGroupMembership
argument_list|(
name|groupIds
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEffectiveGroups ()
specifier|public
name|GroupMembership
name|getEffectiveGroups
parameter_list|()
block|{
return|return
name|groups
return|;
block|}
annotation|@
name|Override
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|username
return|;
block|}
annotation|@
name|Override
DECL|method|getStarredChanges ()
specifier|public
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|getStarredChanges
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getNotificationFilters ()
specifier|public
name|Collection
argument_list|<
name|AccountProjectWatch
argument_list|>
name|getNotificationFilters
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

