begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|reviewdb
operator|.
name|Branch
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
name|PatchSet
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
name|Project
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
name|http
operator|.
name|client
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Link to an external gitweb server. */
end_comment

begin_class
DECL|class|GitwebLink
specifier|public
class|class
name|GitwebLink
block|{
DECL|field|baseUrl
specifier|protected
name|String
name|baseUrl
decl_stmt|;
DECL|field|type
specifier|protected
name|GitWebType
name|type
decl_stmt|;
DECL|method|GitwebLink ()
specifier|protected
name|GitwebLink
parameter_list|()
block|{   }
DECL|method|GitwebLink (final String base, final GitWebType gitWebType)
specifier|public
name|GitwebLink
parameter_list|(
specifier|final
name|String
name|base
parameter_list|,
specifier|final
name|GitWebType
name|gitWebType
parameter_list|)
block|{
name|baseUrl
operator|=
name|base
expr_stmt|;
name|type
operator|=
name|gitWebType
expr_stmt|;
block|}
DECL|method|getLinkName ()
specifier|public
name|String
name|getLinkName
parameter_list|()
block|{
return|return
literal|"("
operator|+
name|type
operator|.
name|getLinkName
argument_list|()
operator|+
literal|")"
return|;
block|}
DECL|method|toRevision (final Project.NameKey project, final PatchSet ps)
specifier|public
name|String
name|toRevision
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|PatchSet
name|ps
parameter_list|)
block|{
name|ParameterizedString
name|pattern
init|=
operator|new
name|ParameterizedString
argument_list|(
name|type
operator|.
name|getRevision
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"commit"
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|baseUrl
operator|+
name|pattern
operator|.
name|replace
argument_list|(
name|p
argument_list|)
return|;
block|}
DECL|method|toProject (final Project.NameKey project)
specifier|public
name|String
name|toProject
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|ParameterizedString
name|pattern
init|=
operator|new
name|ParameterizedString
argument_list|(
name|type
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|baseUrl
operator|+
name|pattern
operator|.
name|replace
argument_list|(
name|p
argument_list|)
return|;
block|}
DECL|method|toBranch (final Branch.NameKey branch)
specifier|public
name|String
name|toBranch
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
block|{
name|ParameterizedString
name|pattern
init|=
operator|new
name|ParameterizedString
argument_list|(
name|type
operator|.
name|getBranch
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|branch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
literal|"branch"
argument_list|,
name|URL
operator|.
name|encodeQueryString
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|baseUrl
operator|+
name|pattern
operator|.
name|replace
argument_list|(
name|p
argument_list|)
return|;
block|}
block|}
end_class

end_unit

