begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|client
operator|.
name|rpc
operator|.
name|NativeString
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
name|client
operator|.
name|rpc
operator|.
name|RestApi
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * A collection of static methods which work on the Gerrit REST API for specific  * changes.  */
end_comment

begin_class
DECL|class|ChangeApi
specifier|public
class|class
name|ChangeApi
block|{
comment|/** Abandon the change, ending its review. */
DECL|method|abandon (int id, String msg, AsyncCallback<ChangeInfo> cb)
specifier|public
specifier|static
name|void
name|abandon
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|msg
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|Input
name|input
init|=
name|Input
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|message
argument_list|(
name|emptyToNull
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|call
argument_list|(
name|id
argument_list|,
literal|"abandon"
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Restore a previously abandoned change to be open again. */
DECL|method|restore (int id, String msg, AsyncCallback<ChangeInfo> cb)
specifier|public
specifier|static
name|void
name|restore
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|msg
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|Input
name|input
init|=
name|Input
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|message
argument_list|(
name|emptyToNull
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|call
argument_list|(
name|id
argument_list|,
literal|"restore"
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Create a new change that reverts the delta caused by this change. */
DECL|method|revert (int id, String msg, AsyncCallback<ChangeInfo> cb)
specifier|public
specifier|static
name|void
name|revert
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|msg
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|Input
name|input
init|=
name|Input
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|message
argument_list|(
name|emptyToNull
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|call
argument_list|(
name|id
argument_list|,
literal|"revert"
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Update the topic of a change. */
DECL|method|topic (int id, String topic, String msg, AsyncCallback<String> cb)
specifier|public
specifier|static
name|void
name|topic
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|topic
parameter_list|,
name|String
name|msg
parameter_list|,
name|AsyncCallback
argument_list|<
name|String
argument_list|>
name|cb
parameter_list|)
block|{
name|RestApi
name|call
init|=
name|call
argument_list|(
name|id
argument_list|,
literal|"topic"
argument_list|)
decl_stmt|;
name|topic
operator|=
name|emptyToNull
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|msg
operator|=
name|emptyToNull
argument_list|(
name|msg
argument_list|)
expr_stmt|;
if|if
condition|(
name|topic
operator|!=
literal|null
operator|||
name|msg
operator|!=
literal|null
condition|)
block|{
name|Input
name|input
init|=
name|Input
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|topic
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|input
operator|.
name|message
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|call
operator|.
name|put
argument_list|(
name|input
argument_list|,
name|NativeString
operator|.
name|unwrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|call
operator|.
name|delete
argument_list|(
name|NativeString
operator|.
name|unwrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|detail (int id, AsyncCallback<ChangeInfo> cb)
specifier|public
specifier|static
name|void
name|detail
parameter_list|(
name|int
name|id
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|detail
argument_list|(
name|id
argument_list|)
operator|.
name|get
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|detail (int id)
specifier|public
specifier|static
name|RestApi
name|detail
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|call
argument_list|(
name|id
argument_list|,
literal|"detail"
argument_list|)
return|;
block|}
DECL|method|revision (int id, String revision)
specifier|public
specifier|static
name|RestApi
name|revision
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"revisions"
argument_list|)
operator|.
name|id
argument_list|(
name|revision
argument_list|)
return|;
block|}
DECL|method|revision (PatchSet.Id id)
specifier|public
specifier|static
name|RestApi
name|revision
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
name|int
name|cn
init|=
name|id
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|revision
init|=
name|RevisionInfoCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|revision
operator|!=
literal|null
condition|)
block|{
return|return
name|revision
argument_list|(
name|cn
argument_list|,
name|revision
argument_list|)
return|;
block|}
return|return
name|change
argument_list|(
name|cn
argument_list|)
operator|.
name|view
argument_list|(
literal|"revisions"
argument_list|)
operator|.
name|id
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|reviewers (int id)
specifier|public
specifier|static
name|RestApi
name|reviewers
parameter_list|(
name|int
name|id
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"reviewers"
argument_list|)
return|;
block|}
DECL|method|suggestReviewers (int id, String q, int n)
specifier|public
specifier|static
name|RestApi
name|suggestReviewers
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|q
parameter_list|,
name|int
name|n
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"suggest_reviewers"
argument_list|)
operator|.
name|addParameter
argument_list|(
literal|"q"
argument_list|,
name|q
argument_list|)
operator|.
name|addParameter
argument_list|(
literal|"n"
argument_list|,
name|n
argument_list|)
return|;
block|}
DECL|method|reviewer (int id, int reviewer)
specifier|public
specifier|static
name|RestApi
name|reviewer
parameter_list|(
name|int
name|id
parameter_list|,
name|int
name|reviewer
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"reviewers"
argument_list|)
operator|.
name|id
argument_list|(
name|reviewer
argument_list|)
return|;
block|}
DECL|method|reviewer (int id, String reviewer)
specifier|public
specifier|static
name|RestApi
name|reviewer
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|reviewer
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"reviewers"
argument_list|)
operator|.
name|id
argument_list|(
name|reviewer
argument_list|)
return|;
block|}
comment|/** Submit a specific revision of a change. */
DECL|method|cherrypick (int id, String commit, String destination, String message, AsyncCallback<ChangeInfo> cb)
specifier|public
specifier|static
name|void
name|cherrypick
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|String
name|destination
parameter_list|,
name|String
name|message
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|CherryPickInput
name|cherryPickInput
init|=
name|CherryPickInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|cherryPickInput
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|cherryPickInput
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|call
argument_list|(
name|id
argument_list|,
name|commit
argument_list|,
literal|"cherrypick"
argument_list|)
operator|.
name|post
argument_list|(
name|cherryPickInput
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Edit commit message for specific revision of a change. */
DECL|method|message (int id, String commit, String message, AsyncCallback<JavaScriptObject> cb)
specifier|public
specifier|static
name|void
name|message
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|String
name|message
parameter_list|,
name|AsyncCallback
argument_list|<
name|JavaScriptObject
argument_list|>
name|cb
parameter_list|)
block|{
name|CherryPickInput
name|input
init|=
name|CherryPickInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|input
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|call
argument_list|(
name|id
argument_list|,
name|commit
argument_list|,
literal|"message"
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Submit a specific revision of a change. */
DECL|method|submit (int id, String commit, AsyncCallback<SubmitInfo> cb)
specifier|public
specifier|static
name|void
name|submit
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|AsyncCallback
argument_list|<
name|SubmitInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|SubmitInput
name|in
init|=
name|SubmitInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|wait_for_merge
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|call
argument_list|(
name|id
argument_list|,
name|commit
argument_list|,
literal|"submit"
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Publish a specific revision of a draft change. */
DECL|method|publish (int id, String commit, AsyncCallback<JavaScriptObject> cb)
specifier|public
specifier|static
name|void
name|publish
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|AsyncCallback
argument_list|<
name|JavaScriptObject
argument_list|>
name|cb
parameter_list|)
block|{
name|JavaScriptObject
name|in
init|=
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|call
argument_list|(
name|id
argument_list|,
name|commit
argument_list|,
literal|"publish"
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Delete a specific draft change. */
DECL|method|deleteChange (int id, AsyncCallback<JavaScriptObject> cb)
specifier|public
specifier|static
name|void
name|deleteChange
parameter_list|(
name|int
name|id
parameter_list|,
name|AsyncCallback
argument_list|<
name|JavaScriptObject
argument_list|>
name|cb
parameter_list|)
block|{
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Delete a specific draft patch set. */
DECL|method|deleteRevision (int id, String commit, AsyncCallback<JavaScriptObject> cb)
specifier|public
specifier|static
name|void
name|deleteRevision
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|AsyncCallback
argument_list|<
name|JavaScriptObject
argument_list|>
name|cb
parameter_list|)
block|{
name|revision
argument_list|(
name|id
argument_list|,
name|commit
argument_list|)
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Rebase a revision onto the branch tip. */
DECL|method|rebase (int id, String commit, AsyncCallback<ChangeInfo> cb)
specifier|public
specifier|static
name|void
name|rebase
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|JavaScriptObject
name|in
init|=
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|call
argument_list|(
name|id
argument_list|,
name|commit
argument_list|,
literal|"rebase"
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|class|Input
specifier|private
specifier|static
class|class
name|Input
extends|extends
name|JavaScriptObject
block|{
DECL|method|topic (String t)
specifier|final
specifier|native
name|void
name|topic
parameter_list|(
name|String
name|t
parameter_list|)
comment|/*-{ if(t)this.topic=t; }-*/
function_decl|;
DECL|method|message (String m)
specifier|final
specifier|native
name|void
name|message
parameter_list|(
name|String
name|m
parameter_list|)
comment|/*-{ if(m)this.message=m; }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|Input
name|create
parameter_list|()
block|{
return|return
operator|(
name|Input
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|Input ()
specifier|protected
name|Input
parameter_list|()
block|{     }
block|}
DECL|class|CherryPickInput
specifier|private
specifier|static
class|class
name|CherryPickInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|static
name|CherryPickInput
name|create
parameter_list|()
block|{
return|return
operator|(
name|CherryPickInput
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|setDestination (String d)
specifier|final
specifier|native
name|void
name|setDestination
parameter_list|(
name|String
name|d
parameter_list|)
comment|/*-{ this.destination = d; }-*/
function_decl|;
DECL|method|setMessage (String m)
specifier|final
specifier|native
name|void
name|setMessage
parameter_list|(
name|String
name|m
parameter_list|)
comment|/*-{ this.message = m; }-*/
function_decl|;
DECL|method|CherryPickInput ()
specifier|protected
name|CherryPickInput
parameter_list|()
block|{     }
block|}
empty_stmt|;
DECL|class|SubmitInput
specifier|private
specifier|static
class|class
name|SubmitInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|wait_for_merge (boolean b)
specifier|final
specifier|native
name|void
name|wait_for_merge
parameter_list|(
name|boolean
name|b
parameter_list|)
comment|/*-{ this.wait_for_merge=b; }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|SubmitInput
name|create
parameter_list|()
block|{
return|return
operator|(
name|SubmitInput
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|SubmitInput ()
specifier|protected
name|SubmitInput
parameter_list|()
block|{     }
block|}
DECL|method|call (int id, String action)
specifier|private
specifier|static
name|RestApi
name|call
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|action
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
name|action
argument_list|)
return|;
block|}
DECL|method|call (int id, String commit, String action)
specifier|private
specifier|static
name|RestApi
name|call
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|commit
parameter_list|,
name|String
name|action
parameter_list|)
block|{
return|return
name|change
argument_list|(
name|id
argument_list|)
operator|.
name|view
argument_list|(
literal|"revisions"
argument_list|)
operator|.
name|id
argument_list|(
name|commit
argument_list|)
operator|.
name|view
argument_list|(
name|action
argument_list|)
return|;
block|}
DECL|method|change (int id)
specifier|public
specifier|static
name|RestApi
name|change
parameter_list|(
name|int
name|id
parameter_list|)
block|{
comment|// TODO Switch to triplet project~branch~id format in URI.
return|return
operator|new
name|RestApi
argument_list|(
literal|"/changes/"
argument_list|)
operator|.
name|id
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|emptyToNull (String str)
specifier|public
specifier|static
name|String
name|emptyToNull
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|==
literal|null
operator|||
name|str
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|str
return|;
block|}
block|}
end_class

end_unit

