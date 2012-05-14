begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|project
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
name|ProjectDetail
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|project
operator|.
name|NoSuchProjectException
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
name|project
operator|.
name|ProjectCache
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Comparator
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
DECL|class|VisibleProjectDetails
class|class
name|VisibleProjectDetails
extends|extends
name|Handler
argument_list|<
name|List
argument_list|<
name|ProjectDetail
argument_list|>
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ()
name|VisibleProjectDetails
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|projectDetailFactory
specifier|private
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|VisibleProjectDetails (final ProjectCache projectCache, final ProjectDetailFactory.Factory projectDetailFactory)
name|VisibleProjectDetails
parameter_list|(
specifier|final
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|projectDetailFactory
operator|=
name|projectDetailFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|List
argument_list|<
name|ProjectDetail
argument_list|>
name|call
parameter_list|()
block|{
name|List
argument_list|<
name|ProjectDetail
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ProjectDetail
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|projectName
range|:
name|projectCache
operator|.
name|all
argument_list|()
control|)
block|{
try|try
block|{
name|result
operator|.
name|add
argument_list|(
name|projectDetailFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{       }
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{       }
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|result
argument_list|,
operator|new
name|Comparator
argument_list|<
name|ProjectDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|ProjectDetail
name|a
parameter_list|,
specifier|final
name|ProjectDetail
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|project
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|project
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

