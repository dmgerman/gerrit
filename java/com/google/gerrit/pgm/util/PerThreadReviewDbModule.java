begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
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
name|events
operator|.
name|LifecycleListener
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|server
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
name|server
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
name|server
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

begin_comment
comment|/**  * Module to bind a single {@link ReviewDb} instance per thread.  *  *<p>New instances are opened on demand, but are closed only at shutdown.  */
end_comment

begin_class
DECL|class|PerThreadReviewDbModule
class|class
name|PerThreadReviewDbModule
extends|extends
name|LifecycleModule
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
DECL|method|PerThreadReviewDbModule (SchemaFactory<ReviewDb> schema)
name|PerThreadReviewDbModule
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|ReviewDb
argument_list|>
name|dbs
init|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|ReviewDb
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ThreadLocal
argument_list|<
name|ReviewDb
argument_list|>
name|localDb
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
name|bind
argument_list|(
name|ReviewDb
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ReviewDb
name|get
parameter_list|()
block|{
name|ReviewDb
name|db
init|=
name|localDb
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|db
operator|=
name|schema
operator|.
name|open
argument_list|()
expr_stmt|;
name|dbs
operator|.
name|add
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|localDb
operator|.
name|set
argument_list|(
name|db
argument_list|)
expr_stmt|;
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
literal|"unable to open ReviewDb"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|db
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|toInstance
argument_list|(
operator|new
name|LifecycleListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
for|for
control|(
name|ReviewDb
name|db
range|:
name|dbs
control|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

