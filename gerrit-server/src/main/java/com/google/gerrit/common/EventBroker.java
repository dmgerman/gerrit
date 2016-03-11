begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicItem
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|Branch
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
name|PatchSet
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
name|events
operator|.
name|ChangeEvent
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
name|events
operator|.
name|Event
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
name|events
operator|.
name|ProjectEvent
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
name|events
operator|.
name|RefEvent
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
name|notedb
operator|.
name|ChangeNotes
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectControl
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
name|project
operator|.
name|ProjectState
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_comment
comment|/** Distributes Events to listeners if they are allowed to see them */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|EventBroker
specifier|public
class|class
name|EventBroker
implements|implements
name|EventDispatcher
block|{
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|DynamicItem
operator|.
name|itemOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|EventDispatcher
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicItem
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|EventDispatcher
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|EventBroker
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Listeners to receive changes as they happen (limited by visibility of    * user).    */
DECL|field|listeners
specifier|protected
specifier|final
name|DynamicSet
argument_list|<
name|UserScopedEventListener
argument_list|>
name|listeners
decl_stmt|;
comment|/** Listeners to receive all changes as they happen. */
DECL|field|unrestrictedListeners
specifier|protected
specifier|final
name|DynamicSet
argument_list|<
name|EventListener
argument_list|>
name|unrestrictedListeners
decl_stmt|;
DECL|field|projectCache
specifier|protected
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|notesFactory
specifier|protected
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|EventBroker (DynamicSet<UserScopedEventListener> listeners, DynamicSet<EventListener> unrestrictedListeners, ProjectCache projectCache, ChangeNotes.Factory notesFactory)
specifier|public
name|EventBroker
parameter_list|(
name|DynamicSet
argument_list|<
name|UserScopedEventListener
argument_list|>
name|listeners
parameter_list|,
name|DynamicSet
argument_list|<
name|EventListener
argument_list|>
name|unrestrictedListeners
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
name|this
operator|.
name|unrestrictedListeners
operator|=
name|unrestrictedListeners
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|notesFactory
operator|=
name|notesFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postEvent (Change change, ChangeEvent event, ReviewDb db)
specifier|public
name|void
name|postEvent
parameter_list|(
name|Change
name|change
parameter_list|,
name|ChangeEvent
name|event
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
name|fireEvent
argument_list|(
name|change
argument_list|,
name|event
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postEvent (Branch.NameKey branchName, RefEvent event)
specifier|public
name|void
name|postEvent
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branchName
parameter_list|,
name|RefEvent
name|event
parameter_list|)
block|{
name|fireEvent
argument_list|(
name|branchName
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postEvent (Project.NameKey projectName, ProjectEvent event)
specifier|public
name|void
name|postEvent
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|ProjectEvent
name|event
parameter_list|)
block|{
name|fireEvent
argument_list|(
name|projectName
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postEvent (Event event, ReviewDb db)
specifier|public
name|void
name|postEvent
parameter_list|(
name|Event
name|event
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
name|fireEvent
argument_list|(
name|event
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
DECL|method|fireEventForUnrestrictedListeners (Event event)
specifier|protected
name|void
name|fireEventForUnrestrictedListeners
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
for|for
control|(
name|EventListener
name|listener
range|:
name|unrestrictedListeners
control|)
block|{
name|listener
operator|.
name|onEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fireEvent (Change change, ChangeEvent event, ReviewDb db)
specifier|protected
name|void
name|fireEvent
parameter_list|(
name|Change
name|change
parameter_list|,
name|ChangeEvent
name|event
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|UserScopedEventListener
name|listener
range|:
name|listeners
control|)
block|{
if|if
condition|(
name|isVisibleTo
argument_list|(
name|change
argument_list|,
name|listener
operator|.
name|getUser
argument_list|()
argument_list|,
name|db
argument_list|)
condition|)
block|{
name|listener
operator|.
name|onEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
name|fireEventForUnrestrictedListeners
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|fireEvent (Project.NameKey project, ProjectEvent event)
specifier|protected
name|void
name|fireEvent
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ProjectEvent
name|event
parameter_list|)
block|{
for|for
control|(
name|UserScopedEventListener
name|listener
range|:
name|listeners
control|)
block|{
if|if
condition|(
name|isVisibleTo
argument_list|(
name|project
argument_list|,
name|listener
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
name|listener
operator|.
name|onEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
name|fireEventForUnrestrictedListeners
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|fireEvent (Branch.NameKey branchName, RefEvent event)
specifier|protected
name|void
name|fireEvent
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branchName
parameter_list|,
name|RefEvent
name|event
parameter_list|)
block|{
for|for
control|(
name|UserScopedEventListener
name|listener
range|:
name|listeners
control|)
block|{
if|if
condition|(
name|isVisibleTo
argument_list|(
name|branchName
argument_list|,
name|listener
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
name|listener
operator|.
name|onEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
name|fireEventForUnrestrictedListeners
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|fireEvent (Event event, ReviewDb db)
specifier|protected
name|void
name|fireEvent
parameter_list|(
name|Event
name|event
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|UserScopedEventListener
name|listener
range|:
name|listeners
control|)
block|{
if|if
condition|(
name|isVisibleTo
argument_list|(
name|event
argument_list|,
name|listener
operator|.
name|getUser
argument_list|()
argument_list|,
name|db
argument_list|)
condition|)
block|{
name|listener
operator|.
name|onEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
name|fireEventForUnrestrictedListeners
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|isVisibleTo (Project.NameKey project, CurrentUser user)
specifier|protected
name|boolean
name|isVisibleTo
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|ProjectState
name|pe
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|project
argument_list|)
decl_stmt|;
if|if
condition|(
name|pe
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|pe
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
operator|.
name|isVisible
argument_list|()
return|;
block|}
DECL|method|isVisibleTo (Change change, CurrentUser user, ReviewDb db)
specifier|protected
name|boolean
name|isVisibleTo
parameter_list|(
name|Change
name|change
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectState
name|pe
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pe
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectControl
name|pc
init|=
name|pe
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
decl_stmt|;
return|return
name|pc
operator|.
name|controlFor
argument_list|(
name|db
argument_list|,
name|change
argument_list|)
operator|.
name|isVisible
argument_list|(
name|db
argument_list|)
return|;
block|}
DECL|method|isVisibleTo (Branch.NameKey branchName, CurrentUser user)
specifier|protected
name|boolean
name|isVisibleTo
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branchName
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|ProjectState
name|pe
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|branchName
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pe
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectControl
name|pc
init|=
name|pe
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
decl_stmt|;
return|return
name|pc
operator|.
name|controlForRef
argument_list|(
name|branchName
argument_list|)
operator|.
name|isVisible
argument_list|()
return|;
block|}
DECL|method|isVisibleTo (Event event, CurrentUser user, ReviewDb db)
specifier|protected
name|boolean
name|isVisibleTo
parameter_list|(
name|Event
name|event
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|event
operator|instanceof
name|RefEvent
condition|)
block|{
name|RefEvent
name|refEvent
init|=
operator|(
name|RefEvent
operator|)
name|event
decl_stmt|;
name|String
name|ref
init|=
name|refEvent
operator|.
name|getRefName
argument_list|()
decl_stmt|;
if|if
condition|(
name|PatchSet
operator|.
name|isChangeRef
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|Change
operator|.
name|Id
name|cid
init|=
name|PatchSet
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|ref
argument_list|)
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|notesFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|refEvent
operator|.
name|getProjectNameKey
argument_list|()
argument_list|,
name|cid
argument_list|)
operator|.
name|getChange
argument_list|()
decl_stmt|;
return|return
name|isVisibleTo
argument_list|(
name|change
argument_list|,
name|user
argument_list|,
name|db
argument_list|)
return|;
block|}
return|return
name|isVisibleTo
argument_list|(
name|refEvent
operator|.
name|getBranchNameKey
argument_list|()
argument_list|,
name|user
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ProjectEvent
condition|)
block|{
return|return
name|isVisibleTo
argument_list|(
operator|(
operator|(
name|ProjectEvent
operator|)
name|event
operator|)
operator|.
name|getProjectNameKey
argument_list|()
argument_list|,
name|user
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

