begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
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
name|ErrorDialog
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
name|GWT
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonException
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
name|client
operator|.
name|ServerUnavailableException
import|;
end_import

begin_comment
comment|/** Abstract callback handling generic error conditions automatically */
end_comment

begin_class
DECL|class|GerritCallback
specifier|public
specifier|abstract
class|class
name|GerritCallback
parameter_list|<
name|T
parameter_list|>
implements|implements
name|AsyncCallback
argument_list|<
name|T
argument_list|>
block|{
DECL|method|onFailure (final Throwable caught)
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
name|isNotSignedIn
argument_list|(
name|caught
argument_list|)
condition|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|errorNotSignedIn
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|caught
operator|instanceof
name|ServerUnavailableException
condition|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|errorServerUnavailable
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|GWT
operator|.
name|log
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" caught "
operator|+
name|caught
argument_list|,
name|caught
argument_list|)
expr_stmt|;
operator|new
name|ErrorDialog
argument_list|(
name|caught
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|isNotSignedIn (final Throwable caught)
specifier|private
specifier|static
name|boolean
name|isNotSignedIn
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
name|caught
operator|instanceof
name|NotSignedInException
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|caught
operator|instanceof
name|RemoteJsonException
operator|&&
name|caught
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|NotSignedInException
operator|.
name|MESSAGE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

