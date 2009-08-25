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
DECL|package|com.google.gerrit.server.ldap
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchControls
import|;
end_import

begin_enum
DECL|enum|SearchScope
specifier|public
enum|enum
name|SearchScope
block|{
comment|// Search only the base DN
comment|//
DECL|enumConstant|OBJECT
name|OBJECT
parameter_list|(
name|SearchControls
operator|.
name|OBJECT_SCOPE
parameter_list|)
operator|,
comment|//
DECL|enumConstant|BASE
constructor|BASE(SearchControls.OBJECT_SCOPE
block|)
enum|,
comment|// Search all entries one level under the base DN
comment|//
comment|// Does not include the base DN, and does not include items below items
comment|// under the base DN.
comment|//
DECL|enumConstant|ONE
name|ONE
argument_list|(
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
argument_list|)
operator|,
comment|// Search all entries under the base DN, including the base DN.
comment|//
DECL|enumConstant|SUBTREE
name|SUBTREE
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
operator|,
comment|//
DECL|enumConstant|SUB
name|SUB
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
enum|;
end_enum

begin_decl_stmt
DECL|field|scope
specifier|private
specifier|final
name|int
name|scope
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|SearchScope (final int scope)
name|SearchScope
argument_list|(
name|final
name|int
name|scope
argument_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
block|;   }
DECL|method|scope ()
name|int
name|scope
argument_list|()
block|{
return|return
name|scope
return|;
block|}
end_expr_stmt

unit|}
end_unit

