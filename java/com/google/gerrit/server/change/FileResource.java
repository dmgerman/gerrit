begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|client
operator|.
name|Patch
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
DECL|class|FileResource
specifier|public
class|class
name|FileResource
implements|implements
name|RestResource
block|{
DECL|field|FILE_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
name|FILE_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|FileResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|rev
specifier|private
specifier|final
name|RevisionResource
name|rev
decl_stmt|;
DECL|field|key
specifier|private
specifier|final
name|Patch
operator|.
name|Key
name|key
decl_stmt|;
DECL|method|FileResource (RevisionResource rev, String name)
specifier|public
name|FileResource
parameter_list|(
name|RevisionResource
name|rev
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|rev
operator|=
name|rev
expr_stmt|;
name|this
operator|.
name|key
operator|=
name|Patch
operator|.
name|key
argument_list|(
name|rev
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getPatchKey ()
specifier|public
name|Patch
operator|.
name|Key
name|getPatchKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|isCacheable ()
specifier|public
name|boolean
name|isCacheable
parameter_list|()
block|{
return|return
name|rev
operator|.
name|isCacheable
argument_list|()
return|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|rev
operator|.
name|getAccountId
argument_list|()
return|;
block|}
DECL|method|getRevision ()
specifier|public
name|RevisionResource
name|getRevision
parameter_list|()
block|{
return|return
name|rev
return|;
block|}
block|}
end_class

end_unit

