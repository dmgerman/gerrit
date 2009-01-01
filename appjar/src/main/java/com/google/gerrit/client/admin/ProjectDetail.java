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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_class
DECL|class|ProjectDetail
specifier|public
class|class
name|ProjectDetail
block|{
DECL|field|project
specifier|protected
name|Project
name|project
decl_stmt|;
DECL|field|ownerGroup
specifier|protected
name|AccountGroup
name|ownerGroup
decl_stmt|;
DECL|method|ProjectDetail ()
specifier|public
name|ProjectDetail
parameter_list|()
block|{   }
DECL|method|load (final ReviewDb db, final Project g)
specifier|public
name|void
name|load
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|Project
name|g
parameter_list|)
throws|throws
name|OrmException
block|{
name|project
operator|=
name|g
expr_stmt|;
name|ownerGroup
operator|=
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|get
argument_list|(
name|project
operator|.
name|getOwnerGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

