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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
package|;
end_package

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
name|List
import|;
end_import

begin_class
DECL|class|GerritConfig
specifier|public
class|class
name|GerritConfig
block|{
DECL|field|canonicalUrl
specifier|private
name|String
name|canonicalUrl
decl_stmt|;
DECL|field|approvalTypes
specifier|private
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|approvalTypes
decl_stmt|;
DECL|method|GerritConfig ()
specifier|public
name|GerritConfig
parameter_list|()
block|{   }
DECL|method|setCanonicalUrl (final String u)
specifier|public
name|void
name|setCanonicalUrl
parameter_list|(
specifier|final
name|String
name|u
parameter_list|)
block|{
name|canonicalUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getCanonicalUrl ()
specifier|public
name|String
name|getCanonicalUrl
parameter_list|()
block|{
return|return
name|canonicalUrl
return|;
block|}
DECL|method|add (final ApprovalType t)
specifier|public
name|void
name|add
parameter_list|(
specifier|final
name|ApprovalType
name|t
parameter_list|)
block|{
name|initApprovalTypes
argument_list|()
expr_stmt|;
name|approvalTypes
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
DECL|method|getApprovalTypes ()
specifier|public
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|getApprovalTypes
parameter_list|()
block|{
name|initApprovalTypes
argument_list|()
expr_stmt|;
return|return
name|approvalTypes
return|;
block|}
DECL|method|initApprovalTypes ()
specifier|private
name|void
name|initApprovalTypes
parameter_list|()
block|{
if|if
condition|(
name|approvalTypes
operator|==
literal|null
condition|)
block|{
name|approvalTypes
operator|=
operator|new
name|ArrayList
argument_list|<
name|ApprovalType
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

