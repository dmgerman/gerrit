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
name|gerrit
operator|.
name|server
operator|.
name|RequestCleanup
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_comment
comment|/** Provides {@link ReviewDb} database handle live only for this request. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|RequestScopedReviewDbProvider
specifier|final
class|class
name|RequestScopedReviewDbProvider
implements|implements
name|Provider
argument_list|<
name|ReviewDb
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
DECL|field|cleanup
specifier|private
specifier|final
name|Provider
argument_list|<
name|RequestCleanup
argument_list|>
name|cleanup
decl_stmt|;
annotation|@
name|Inject
DECL|method|RequestScopedReviewDbProvider (final SchemaFactory<ReviewDb> schema, final Provider<RequestCleanup> cleanup)
name|RequestScopedReviewDbProvider
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|RequestCleanup
argument_list|>
name|cleanup
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|cleanup
operator|=
name|cleanup
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ReviewDb
name|get
parameter_list|()
block|{
specifier|final
name|ReviewDb
name|c
decl_stmt|;
try|try
block|{
name|c
operator|=
name|schema
operator|.
name|open
argument_list|()
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
literal|"Cannot open ReviewDb"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|cleanup
operator|.
name|get
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

