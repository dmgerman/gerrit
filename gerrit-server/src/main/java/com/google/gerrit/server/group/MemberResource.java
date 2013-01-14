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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|server
operator|.
name|IdentifiedUser
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
name|AccountResource
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

begin_class
DECL|class|MemberResource
specifier|public
class|class
name|MemberResource
extends|extends
name|AccountResource
block|{
DECL|field|MEMBER_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|MemberResource
argument_list|>
argument_list|>
name|MEMBER_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|MemberResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|group
specifier|private
specifier|final
name|GroupResource
name|group
decl_stmt|;
DECL|method|MemberResource (GroupResource group, IdentifiedUser user)
specifier|public
name|MemberResource
parameter_list|(
name|GroupResource
name|group
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|super
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|GroupResource
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
block|}
end_class

end_unit

