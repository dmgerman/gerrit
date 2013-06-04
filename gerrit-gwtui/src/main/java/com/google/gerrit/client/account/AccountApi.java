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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|VoidResult
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
comment|/**  * A collection of static methods which work on the Gerrit REST API for specific  * accounts.  */
end_comment

begin_class
DECL|class|AccountApi
specifier|public
class|class
name|AccountApi
block|{
comment|/** Register a new email address */
DECL|method|registerEmail (String account, String email, AsyncCallback<NativeString> cb)
specifier|public
specifier|static
name|void
name|registerEmail
parameter_list|(
name|String
name|account
parameter_list|,
name|String
name|email
parameter_list|,
name|AsyncCallback
argument_list|<
name|NativeString
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
operator|new
name|RestApi
argument_list|(
literal|"/accounts/"
argument_list|)
operator|.
name|id
argument_list|(
name|account
argument_list|)
operator|.
name|view
argument_list|(
literal|"emails"
argument_list|)
operator|.
name|id
argument_list|(
name|email
argument_list|)
operator|.
name|ifNoneMatch
argument_list|()
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Generate a new HTTP password */
DECL|method|generateHttpPassword (String account, AsyncCallback<NativeString> cb)
specifier|public
specifier|static
name|void
name|generateHttpPassword
parameter_list|(
name|String
name|account
parameter_list|,
name|AsyncCallback
argument_list|<
name|NativeString
argument_list|>
name|cb
parameter_list|)
block|{
name|HttpPasswordInput
name|in
init|=
name|HttpPasswordInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|generate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|new
name|RestApi
argument_list|(
literal|"/accounts/"
argument_list|)
operator|.
name|id
argument_list|(
name|account
argument_list|)
operator|.
name|view
argument_list|(
literal|"password.http"
argument_list|)
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Clear HTTP password */
DECL|method|clearHttpPassword (String account, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|clearHttpPassword
parameter_list|(
name|String
name|account
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
operator|new
name|RestApi
argument_list|(
literal|"/accounts/"
argument_list|)
operator|.
name|id
argument_list|(
name|account
argument_list|)
operator|.
name|view
argument_list|(
literal|"password.http"
argument_list|)
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|class|HttpPasswordInput
specifier|private
specifier|static
class|class
name|HttpPasswordInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|generate (boolean g)
specifier|final
specifier|native
name|void
name|generate
parameter_list|(
name|boolean
name|g
parameter_list|)
comment|/*-{ if(g)this.generate=g; }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|HttpPasswordInput
name|create
parameter_list|()
block|{
return|return
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
return|;
block|}
DECL|method|HttpPasswordInput ()
specifier|protected
name|HttpPasswordInput
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

