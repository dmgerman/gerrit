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
name|entities
operator|.
name|FixReplacement
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
DECL|class|FixResource
specifier|public
class|class
name|FixResource
implements|implements
name|RestResource
block|{
DECL|field|FIX_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|FixResource
argument_list|>
argument_list|>
name|FIX_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|FixResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|fixReplacements
specifier|private
specifier|final
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|fixReplacements
decl_stmt|;
DECL|field|revisionResource
specifier|private
specifier|final
name|RevisionResource
name|revisionResource
decl_stmt|;
DECL|method|FixResource (RevisionResource revisionResource, List<FixReplacement> fixReplacements)
specifier|public
name|FixResource
parameter_list|(
name|RevisionResource
name|revisionResource
parameter_list|,
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|fixReplacements
parameter_list|)
block|{
name|this
operator|.
name|fixReplacements
operator|=
name|fixReplacements
expr_stmt|;
name|this
operator|.
name|revisionResource
operator|=
name|revisionResource
expr_stmt|;
block|}
DECL|method|getFixReplacements ()
specifier|public
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|getFixReplacements
parameter_list|()
block|{
return|return
name|fixReplacements
return|;
block|}
DECL|method|getRevisionResource ()
specifier|public
name|RevisionResource
name|getRevisionResource
parameter_list|()
block|{
return|return
name|revisionResource
return|;
block|}
block|}
end_class

end_unit

