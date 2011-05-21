begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|common
operator|.
name|data
operator|.
name|ApprovalType
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
name|data
operator|.
name|ApprovalTypes
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
name|ApprovalCategory
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
name|ApprovalCategoryValue
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
name|SchemaFactory
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
name|ProvisionException
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
name|List
import|;
end_import

begin_class
DECL|class|ApprovalTypesProvider
specifier|public
class|class
name|ApprovalTypesProvider
implements|implements
name|Provider
argument_list|<
name|ApprovalTypes
argument_list|>
block|{
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Inject
DECL|method|ApprovalTypesProvider (final SchemaFactory<ReviewDb> sf)
name|ApprovalTypesProvider
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|)
block|{
name|schema
operator|=
name|sf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ApprovalTypes
name|get
parameter_list|()
block|{
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|types
init|=
operator|new
name|ArrayList
argument_list|<
name|ApprovalType
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
specifier|final
name|ApprovalCategory
name|c
range|:
name|db
operator|.
name|approvalCategories
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
specifier|final
name|List
argument_list|<
name|ApprovalCategoryValue
argument_list|>
name|values
init|=
name|db
operator|.
name|approvalCategoryValues
argument_list|()
operator|.
name|byCategory
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
name|types
operator|.
name|add
argument_list|(
operator|new
name|ApprovalType
argument_list|(
name|c
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot query approval categories"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|ApprovalTypes
argument_list|(
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|types
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

