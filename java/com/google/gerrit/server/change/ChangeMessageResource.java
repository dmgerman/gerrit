begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
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
name|extensions
operator|.
name|common
operator|.
name|ChangeMessageInfo
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
name|RestResource
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
name|inject
operator|.
name|TypeLiteral
import|;
end_import

begin_comment
comment|/** A change message resource. */
end_comment

begin_class
DECL|class|ChangeMessageResource
specifier|public
class|class
name|ChangeMessageResource
implements|implements
name|RestResource
block|{
DECL|field|CHANGE_MESSAGE_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|ChangeMessageResource
argument_list|>
argument_list|>
name|CHANGE_MESSAGE_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|ChangeMessageResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|changeResource
specifier|private
specifier|final
name|ChangeResource
name|changeResource
decl_stmt|;
DECL|field|changeMessage
specifier|private
specifier|final
name|ChangeMessageInfo
name|changeMessage
decl_stmt|;
DECL|field|changeMessageIndex
specifier|private
specifier|final
name|int
name|changeMessageIndex
decl_stmt|;
DECL|method|ChangeMessageResource ( ChangeResource changeResource, ChangeMessageInfo changeMessage, int changeMessageIndex)
specifier|public
name|ChangeMessageResource
parameter_list|(
name|ChangeResource
name|changeResource
parameter_list|,
name|ChangeMessageInfo
name|changeMessage
parameter_list|,
name|int
name|changeMessageIndex
parameter_list|)
block|{
name|this
operator|.
name|changeResource
operator|=
name|changeResource
expr_stmt|;
name|this
operator|.
name|changeMessage
operator|=
name|changeMessage
expr_stmt|;
name|this
operator|.
name|changeMessageIndex
operator|=
name|changeMessageIndex
expr_stmt|;
block|}
DECL|method|getChangeResource ()
specifier|public
name|ChangeResource
name|getChangeResource
parameter_list|()
block|{
return|return
name|changeResource
return|;
block|}
DECL|method|getChangeMessage ()
specifier|public
name|ChangeMessageInfo
name|getChangeMessage
parameter_list|()
block|{
return|return
name|changeMessage
return|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|changeResource
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getChangeMessageId ()
specifier|public
name|String
name|getChangeMessageId
parameter_list|()
block|{
return|return
name|changeMessage
operator|.
name|id
return|;
block|}
comment|/**    * Gets the index of the change message among all messages of the change sorted by creation time.    *    * @return the index of the change message.    */
DECL|method|getChangeMessageIndex ()
specifier|public
name|int
name|getChangeMessageIndex
parameter_list|()
block|{
return|return
name|changeMessageIndex
return|;
block|}
block|}
end_class

end_unit

