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
name|gwtjsonrpc
operator|.
name|common
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
DECL|field|URI
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"/changes/"
decl_stmt|;
comment|/**    * Sends a REST call to abandon a change and notify a callback. TODO: switch    * to use the new id triplet (project~branch~change) once that data is    * available to the UI.    */
DECL|method|abandon (int changeId, String message, AsyncCallback<ChangeInfo> callback)
specifier|public
specifier|static
name|void
name|abandon
parameter_list|(
name|int
name|changeId
parameter_list|,
name|String
name|message
parameter_list|,
name|AsyncCallback
argument_list|<
name|ChangeInfo
argument_list|>
name|callback
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
name|setMessage
argument_list|(
name|emptyToNull
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|RestApi
argument_list|(
name|URI
operator|+
name|changeId
operator|+
literal|"/abandon"
argument_list|)
operator|.
name|data
argument_list|(
name|input
argument_list|)
operator|.
name|post
argument_list|(
name|callback
argument_list|)
expr_stmt|;
block|}
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
name|setTopic
argument_list|(
name|emptyToNull
argument_list|(
name|topic
argument_list|)
argument_list|)
expr_stmt|;
name|input
operator|.
name|setMessage
argument_list|(
name|emptyToNull
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|RestApi
argument_list|(
name|URI
operator|+
name|id
operator|+
literal|"/topic"
argument_list|)
operator|.
name|data
argument_list|(
name|input
argument_list|)
operator|.
name|put
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
DECL|class|Input
specifier|private
specifier|static
class|class
name|Input
extends|extends
name|JavaScriptObject
block|{
DECL|method|setTopic (String t)
specifier|final
specifier|native
name|void
name|setTopic
parameter_list|(
name|String
name|t
parameter_list|)
comment|/*-{ this.topic = t; }-*/
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
name|JavaScriptObject
operator|.
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
DECL|method|emptyToNull (String str)
specifier|private
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

