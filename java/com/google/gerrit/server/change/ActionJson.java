begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|ImmutableMap
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
name|Iterables
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
name|Nullable
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
name|api
operator|.
name|changes
operator|.
name|ActionVisitor
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
name|common
operator|.
name|ActionInfo
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
name|common
operator|.
name|ChangeInfo
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
name|common
operator|.
name|RevisionInfo
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
name|DynamicMap
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|webui
operator|.
name|PrivateInternals_UiActionDescription
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
name|webui
operator|.
name|UiAction
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
name|extensions
operator|.
name|webui
operator|.
name|UiActions
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
name|Provider
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_class
annotation|@
name|Singleton
DECL|class|ActionJson
specifier|public
class|class
name|ActionJson
block|{
DECL|field|revisionViews
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|RevisionResource
argument_list|>
argument_list|>
name|revisionViews
decl_stmt|;
DECL|field|changeJsonFactory
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
name|changeJsonFactory
decl_stmt|;
DECL|field|changeResourceFactory
specifier|private
specifier|final
name|ChangeResource
operator|.
name|Factory
name|changeResourceFactory
decl_stmt|;
DECL|field|uiActions
specifier|private
specifier|final
name|UiActions
name|uiActions
decl_stmt|;
DECL|field|changeViews
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|changeViews
decl_stmt|;
DECL|field|visitorSet
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ActionVisitor
argument_list|>
name|visitorSet
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|ActionJson ( DynamicMap<RestView<RevisionResource>> views, ChangeJson.Factory changeJsonFactory, ChangeResource.Factory changeResourceFactory, UiActions uiActions, DynamicMap<RestView<ChangeResource>> changeViews, DynamicSet<ActionVisitor> visitorSet, Provider<CurrentUser> userProvider)
name|ActionJson
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|RevisionResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|ChangeJson
operator|.
name|Factory
name|changeJsonFactory
parameter_list|,
name|ChangeResource
operator|.
name|Factory
name|changeResourceFactory
parameter_list|,
name|UiActions
name|uiActions
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ChangeResource
argument_list|>
argument_list|>
name|changeViews
parameter_list|,
name|DynamicSet
argument_list|<
name|ActionVisitor
argument_list|>
name|visitorSet
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|)
block|{
name|this
operator|.
name|revisionViews
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|changeJsonFactory
operator|=
name|changeJsonFactory
expr_stmt|;
name|this
operator|.
name|changeResourceFactory
operator|=
name|changeResourceFactory
expr_stmt|;
name|this
operator|.
name|uiActions
operator|=
name|uiActions
expr_stmt|;
name|this
operator|.
name|changeViews
operator|=
name|changeViews
expr_stmt|;
name|this
operator|.
name|visitorSet
operator|=
name|visitorSet
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
block|}
DECL|method|format (RevisionResource rsrc)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|format
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
block|{
name|ChangeInfo
name|changeInfo
init|=
literal|null
decl_stmt|;
name|RevisionInfo
name|revisionInfo
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
init|=
name|visitors
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|visitors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|changeInfo
operator|=
name|changeJson
argument_list|()
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
name|revisionInfo
operator|=
name|requireNonNull
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|changeInfo
operator|.
name|revisions
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|changeInfo
operator|.
name|revisions
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|toActionMap
argument_list|(
name|rsrc
argument_list|,
name|visitors
argument_list|,
name|changeInfo
argument_list|,
name|revisionInfo
argument_list|)
return|;
block|}
DECL|method|changeJson ()
specifier|private
name|ChangeJson
name|changeJson
parameter_list|()
block|{
return|return
name|changeJsonFactory
operator|.
name|noOptions
argument_list|()
return|;
block|}
DECL|method|visitors ()
specifier|private
name|ArrayList
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|visitorSet
argument_list|)
return|;
block|}
DECL|method|addChangeActions (ChangeInfo to, ChangeNotes notes)
specifier|public
name|ChangeInfo
name|addChangeActions
parameter_list|(
name|ChangeInfo
name|to
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
block|{
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
init|=
name|visitors
argument_list|()
decl_stmt|;
name|to
operator|.
name|actions
operator|=
name|toActionMap
argument_list|(
name|notes
argument_list|,
name|visitors
argument_list|,
name|copy
argument_list|(
name|visitors
argument_list|,
name|to
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|to
return|;
block|}
DECL|method|addRevisionActions ( @ullable ChangeInfo changeInfo, RevisionInfo to, RevisionResource rsrc)
specifier|public
name|RevisionInfo
name|addRevisionActions
parameter_list|(
annotation|@
name|Nullable
name|ChangeInfo
name|changeInfo
parameter_list|,
name|RevisionInfo
name|to
parameter_list|,
name|RevisionResource
name|rsrc
parameter_list|)
block|{
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
init|=
name|visitors
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|visitors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|changeInfo
operator|!=
literal|null
condition|)
block|{
name|changeInfo
operator|=
name|copy
argument_list|(
name|visitors
argument_list|,
name|changeInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|changeInfo
operator|=
name|changeJson
argument_list|()
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
block|}
block|}
name|to
operator|.
name|actions
operator|=
name|toActionMap
argument_list|(
name|rsrc
argument_list|,
name|visitors
argument_list|,
name|changeInfo
argument_list|,
name|copy
argument_list|(
name|visitors
argument_list|,
name|to
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|to
return|;
block|}
DECL|method|copy (List<ActionVisitor> visitors, ChangeInfo changeInfo)
specifier|private
name|ChangeInfo
name|copy
parameter_list|(
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
parameter_list|,
name|ChangeInfo
name|changeInfo
parameter_list|)
block|{
if|if
condition|(
name|visitors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Include all fields from ChangeJson#toChangeInfoImpl that are not protected by any
comment|// ListChangesOptions.
name|ChangeInfo
name|copy
init|=
operator|new
name|ChangeInfo
argument_list|()
decl_stmt|;
name|copy
operator|.
name|project
operator|=
name|changeInfo
operator|.
name|project
expr_stmt|;
name|copy
operator|.
name|branch
operator|=
name|changeInfo
operator|.
name|branch
expr_stmt|;
name|copy
operator|.
name|topic
operator|=
name|changeInfo
operator|.
name|topic
expr_stmt|;
name|copy
operator|.
name|assignee
operator|=
name|changeInfo
operator|.
name|assignee
expr_stmt|;
name|copy
operator|.
name|hashtags
operator|=
name|changeInfo
operator|.
name|hashtags
expr_stmt|;
name|copy
operator|.
name|changeId
operator|=
name|changeInfo
operator|.
name|changeId
expr_stmt|;
name|copy
operator|.
name|submitType
operator|=
name|changeInfo
operator|.
name|submitType
expr_stmt|;
name|copy
operator|.
name|mergeable
operator|=
name|changeInfo
operator|.
name|mergeable
expr_stmt|;
name|copy
operator|.
name|insertions
operator|=
name|changeInfo
operator|.
name|insertions
expr_stmt|;
name|copy
operator|.
name|deletions
operator|=
name|changeInfo
operator|.
name|deletions
expr_stmt|;
name|copy
operator|.
name|hasReviewStarted
operator|=
name|changeInfo
operator|.
name|hasReviewStarted
expr_stmt|;
name|copy
operator|.
name|isPrivate
operator|=
name|changeInfo
operator|.
name|isPrivate
expr_stmt|;
name|copy
operator|.
name|subject
operator|=
name|changeInfo
operator|.
name|subject
expr_stmt|;
name|copy
operator|.
name|status
operator|=
name|changeInfo
operator|.
name|status
expr_stmt|;
name|copy
operator|.
name|owner
operator|=
name|changeInfo
operator|.
name|owner
expr_stmt|;
name|copy
operator|.
name|created
operator|=
name|changeInfo
operator|.
name|created
expr_stmt|;
name|copy
operator|.
name|updated
operator|=
name|changeInfo
operator|.
name|updated
expr_stmt|;
name|copy
operator|.
name|_number
operator|=
name|changeInfo
operator|.
name|_number
expr_stmt|;
name|copy
operator|.
name|requirements
operator|=
name|changeInfo
operator|.
name|requirements
expr_stmt|;
name|copy
operator|.
name|revertOf
operator|=
name|changeInfo
operator|.
name|revertOf
expr_stmt|;
name|copy
operator|.
name|submissionId
operator|=
name|changeInfo
operator|.
name|submissionId
expr_stmt|;
name|copy
operator|.
name|starred
operator|=
name|changeInfo
operator|.
name|starred
expr_stmt|;
name|copy
operator|.
name|stars
operator|=
name|changeInfo
operator|.
name|stars
expr_stmt|;
name|copy
operator|.
name|submitted
operator|=
name|changeInfo
operator|.
name|submitted
expr_stmt|;
name|copy
operator|.
name|submitter
operator|=
name|changeInfo
operator|.
name|submitter
expr_stmt|;
name|copy
operator|.
name|unresolvedCommentCount
operator|=
name|changeInfo
operator|.
name|unresolvedCommentCount
expr_stmt|;
name|copy
operator|.
name|workInProgress
operator|=
name|changeInfo
operator|.
name|workInProgress
expr_stmt|;
name|copy
operator|.
name|id
operator|=
name|changeInfo
operator|.
name|id
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|copy (List<ActionVisitor> visitors, RevisionInfo revisionInfo)
specifier|private
name|RevisionInfo
name|copy
parameter_list|(
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
parameter_list|,
name|RevisionInfo
name|revisionInfo
parameter_list|)
block|{
if|if
condition|(
name|visitors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Include all fields from RevisionJson#toRevisionInfo that are not protected by any
comment|// ListChangesOptions.
name|RevisionInfo
name|copy
init|=
operator|new
name|RevisionInfo
argument_list|()
decl_stmt|;
name|copy
operator|.
name|isCurrent
operator|=
name|revisionInfo
operator|.
name|isCurrent
expr_stmt|;
name|copy
operator|.
name|_number
operator|=
name|revisionInfo
operator|.
name|_number
expr_stmt|;
name|copy
operator|.
name|ref
operator|=
name|revisionInfo
operator|.
name|ref
expr_stmt|;
name|copy
operator|.
name|created
operator|=
name|revisionInfo
operator|.
name|created
expr_stmt|;
name|copy
operator|.
name|uploader
operator|=
name|revisionInfo
operator|.
name|uploader
expr_stmt|;
name|copy
operator|.
name|fetch
operator|=
name|revisionInfo
operator|.
name|fetch
expr_stmt|;
name|copy
operator|.
name|kind
operator|=
name|revisionInfo
operator|.
name|kind
expr_stmt|;
name|copy
operator|.
name|description
operator|=
name|revisionInfo
operator|.
name|description
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|toActionMap ( ChangeNotes notes, List<ActionVisitor> visitors, ChangeInfo changeInfo)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|toActionMap
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
parameter_list|,
name|ChangeInfo
name|changeInfo
parameter_list|)
block|{
name|CurrentUser
name|user
init|=
name|userProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|out
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
return|return
name|out
return|;
block|}
name|Iterable
argument_list|<
name|UiAction
operator|.
name|Description
argument_list|>
name|descs
init|=
name|uiActions
operator|.
name|from
argument_list|(
name|changeViews
argument_list|,
name|changeResourceFactory
operator|.
name|create
argument_list|(
name|notes
argument_list|,
name|user
argument_list|)
argument_list|)
decl_stmt|;
comment|// The followup action is a client-side only operation that does not
comment|// have a server side handler. It must be manually registered into the
comment|// resulting action map.
if|if
condition|(
operator|!
name|notes
operator|.
name|getChange
argument_list|()
operator|.
name|isAbandoned
argument_list|()
condition|)
block|{
name|UiAction
operator|.
name|Description
name|descr
init|=
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
decl_stmt|;
name|PrivateInternals_UiActionDescription
operator|.
name|setId
argument_list|(
name|descr
argument_list|,
literal|"followup"
argument_list|)
expr_stmt|;
name|PrivateInternals_UiActionDescription
operator|.
name|setMethod
argument_list|(
name|descr
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|descr
operator|.
name|setTitle
argument_list|(
literal|"Create follow-up change"
argument_list|)
expr_stmt|;
name|descr
operator|.
name|setLabel
argument_list|(
literal|"Follow-Up"
argument_list|)
expr_stmt|;
name|descs
operator|=
name|Iterables
operator|.
name|concat
argument_list|(
name|descs
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|descr
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ACTION
label|:
for|for
control|(
name|UiAction
operator|.
name|Description
name|d
range|:
name|descs
control|)
block|{
name|ActionInfo
name|actionInfo
init|=
operator|new
name|ActionInfo
argument_list|(
name|d
argument_list|)
decl_stmt|;
for|for
control|(
name|ActionVisitor
name|visitor
range|:
name|visitors
control|)
block|{
if|if
condition|(
operator|!
name|visitor
operator|.
name|visit
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|,
name|actionInfo
argument_list|,
name|changeInfo
argument_list|)
condition|)
block|{
continue|continue
name|ACTION
continue|;
block|}
block|}
name|out
operator|.
name|put
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|,
name|actionInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
DECL|method|toActionMap ( RevisionResource rsrc, List<ActionVisitor> visitors, ChangeInfo changeInfo, RevisionInfo revisionInfo)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|toActionMap
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|List
argument_list|<
name|ActionVisitor
argument_list|>
name|visitors
parameter_list|,
name|ChangeInfo
name|changeInfo
parameter_list|,
name|RevisionInfo
name|revisionInfo
parameter_list|)
block|{
if|if
condition|(
operator|!
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|out
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ACTION
label|:
for|for
control|(
name|UiAction
operator|.
name|Description
name|d
range|:
name|uiActions
operator|.
name|from
argument_list|(
name|revisionViews
argument_list|,
name|rsrc
argument_list|)
control|)
block|{
name|ActionInfo
name|actionInfo
init|=
operator|new
name|ActionInfo
argument_list|(
name|d
argument_list|)
decl_stmt|;
for|for
control|(
name|ActionVisitor
name|visitor
range|:
name|visitors
control|)
block|{
if|if
condition|(
operator|!
name|visitor
operator|.
name|visit
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|,
name|actionInfo
argument_list|,
name|changeInfo
argument_list|,
name|revisionInfo
argument_list|)
condition|)
block|{
continue|continue
name|ACTION
continue|;
block|}
block|}
name|out
operator|.
name|put
argument_list|(
name|d
operator|.
name|getId
argument_list|()
argument_list|,
name|actionInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
block|}
end_class

end_unit

