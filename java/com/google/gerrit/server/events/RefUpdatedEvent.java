begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
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
name|base
operator|.
name|Supplier
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
name|entities
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
name|server
operator|.
name|data
operator|.
name|AccountAttribute
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
name|data
operator|.
name|RefUpdateAttribute
import|;
end_import

begin_class
DECL|class|RefUpdatedEvent
specifier|public
class|class
name|RefUpdatedEvent
extends|extends
name|RefEvent
block|{
DECL|field|TYPE
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"ref-updated"
decl_stmt|;
DECL|field|submitter
specifier|public
name|Supplier
argument_list|<
name|AccountAttribute
argument_list|>
name|submitter
decl_stmt|;
DECL|field|refUpdate
specifier|public
name|Supplier
argument_list|<
name|RefUpdateAttribute
argument_list|>
name|refUpdate
decl_stmt|;
DECL|method|RefUpdatedEvent ()
specifier|public
name|RefUpdatedEvent
parameter_list|()
block|{
name|super
argument_list|(
name|TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectNameKey
parameter_list|()
block|{
return|return
name|Project
operator|.
name|nameKey
argument_list|(
name|refUpdate
operator|.
name|get
argument_list|()
operator|.
name|project
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|refUpdate
operator|.
name|get
argument_list|()
operator|.
name|refName
return|;
block|}
block|}
end_class

end_unit

